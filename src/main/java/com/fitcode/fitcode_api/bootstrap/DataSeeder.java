package com.fitcode.fitcode_api.bootstrap;

import com.fitcode.fitcode_api.models.*;
import com.fitcode.fitcode_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final WorkoutRepository workoutRepository;
    private final WorkoutVideoRepository workoutVideoRepository;
    private final RoutineRepository routineRepository;
    private final RoutineWorkoutRepository routineWorkoutRepository;
    private final WorkoutCategoryRepository categoryRepository;
    private final UserWorkoutProgressRepository userWorkoutProgressRepository;
    // optional repositories
    private final UserRoutineRepository userRoutineRepository; // puede que exista o no
    private final UserRoutineSessionRepository userRoutineSessionRepository; // puede que exista o no
    private final RoleRepository roleRepository; // para asignar rol admin al usuario seed
    private final UserRepository userRepository; // para asignar autor a rutinas seed

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // sólo correr si tablas vacías (evita duplicados)
        if (workoutRepository.count() > 0 || workoutVideoRepository.count() > 0 || routineRepository.count() > 0) {
            System.out.println("DataSeeder: tablas ya contienen datos, saltando seed.");
            return;
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        List<Workout> workouts;
        // 1) Categories (si no hay)
        List<WorkoutCategory> categories = ensureCategories();

        if (workoutRepository.count() == 0) {
            System.out.println("DataSeeder: Generando datos de ejemplo...");
            // 2) Generate 50 workouts
            workouts = IntStream.rangeClosed(1, 50)
                    .mapToObj(i -> {
                        Workout w = Workout.builder()
                                .title("Workout " + i + " - " + randomName())
                                .description("Rutina enfocada en " + randomMuscleGroup())
                                .difficulty(randomDifficulty())
                                .mainMuscleGroup(randomMuscleGroup())
                                .equipment(randomEquipment())
                                .sets(randomInt(3, 5))
                                .reps(randomInt(8, 15))
                                .durationSeconds(randomInt(30, 300))
                                .thumbnailUrl("https://picsum.photos/seed/workout" + i + "/600/400")
                                .isPublic(1)
                                .metadata("{\"tag\":\"seed\"}")
                                .build();
                        // asignar categoría aleatoria
                        w.setCategory(categories.get(randomInt(0, categories.size() - 1)));
                        return w;
                    }).map(workoutRepository::save).collect(Collectors.toList());

            System.out.println("Seed: created " + workouts.size() + " workouts.");
        } else {
            workouts = workoutRepository.findAll();
            System.out.println("DataSeeder: workouts ya existen (" + workouts.size()
                    + " encontrados), saltando creación de workouts.");
        }

        // 3) Generate 80 workout videos (some linked to workouts)
        // lista de youtube ids (ejemplos reales o placeholders)
        String[] youtubeIds = {
                "dQw4w9WgXcQ", "3JZ_D3ELwOQ", "L_jWHffIx5E", "kXYiU_JCYtU", "RgKAFK5djSk",
                "hT_nvWreIhg", "9bZkp7q19f0", "fJ9rUzIMcZQ", "60ItHLz5WEA", "60C1f1rT4fM",
                "VbfpW0pbvaU", "u9Dg-g7t2l4", "eVTXPUF4Oz4", "2Vv-BfVoq4g", "YQHsXMglC9A",
                "CevxZvSJLk8", "kJQP7kiw5Fk", "uelHwf8o7_U", "SlPhMPnQ58k", "09R8_2nJtjg"
        };

        List<WorkoutVideo> videos = new ArrayList<>();
        List<String> roles = Arrays.asList("admin", "user", "coach");

        for (int i = 0; i <= roles.size() - 1; i++) {
            Role roleCreated = roleRepository.findByName(roles.get(i))
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name("admin")
                                    .description("Administrador")
                                    .createdAt(LocalDateTime.now())
                                    .build()));

            System.out.println("Seed: role '" + roleCreated.getName() + "' created with id " + roleCreated.getId());
        }

        Role role = roleRepository.findByName("admin")
                .orElseThrow(() -> new IllegalStateException("Role 'admin' should have been created"));

        // set author user id 3 (lazy User entity required)
        User author = userRepository.findByEmail("ferwin@fitcode.com")
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email("ferwin@fitcode.com")
                                .fullName("Ferwin Arias")
                                .passwordHash(passwordEncoder.encode("securepass123"))
                                .displayName("Administrador")
                                .role(role)
                                .sex("F")
                                .heightCm(170)
                                .weightKg(70.5)
                                .metadata("{ \"seed\":\"true\" }")
                                .dateOfBirth(LocalDate.of(2000, 5, 5))
                                .createdAt(LocalDateTime.now())
                                .build()));

        if (workoutVideoRepository.count() == 0) {
            System.out.println("DataSeeder: creando workout videos...");
            for (int i = 1; i <= 80; i++) {
                String vid = youtubeIds[randomInt(0, youtubeIds.length - 1)];
                // embed url for iframe consumption
                String embedUrl = "https://www.youtube.com/embed/" + vid + "?rel=0";
                WorkoutVideo v = WorkoutVideo.builder()
                        .title("Video " + i + " - " + randomName())
                        .url(embedUrl)
                        .durationSeconds(randomInt(30, 900))
                        .isPublic(1)
                        .videoType(randomVideoType())
                        .resolution(randomResolution())
                        .build();
                // optionally link to a workout in ~60% cases
                if (Math.random() < 0.6) {
                    v.setWorkout(workouts.get(randomInt(0, workouts.size() - 1)));
                }
                videos.add(workoutVideoRepository.save(v));
            }
            System.out.println("Seed: created " + videos.size() + " workout videos.");
        }
        List<Routine> routines;
        // 4) Generate 30 routines (author user id = 3)
        if (routineRepository.count() == 0) {
            routines = IntStream.rangeClosed(1, 30).mapToObj(i -> {
                Routine r = Routine.builder()
                        .title("Rutina #" + i + " - " + randomRoutineName())
                        .description("Rutina diseñada para nivel " + randomDifficulty())
                        .difficulty(randomDifficulty())
                        .durationMinutes(randomInt(10, 60))
                        .isPublic(1)
                        .metadata("{\"seed\":\"true\"}")
                        .thumbnailUrl(randomRoutineThumbnail())
                        .author(author)
                        .build();

                return routineRepository.save(r);
            }).collect(Collectors.toList());

            System.out.println("Seed: created " + routines.size() + " routines.");

        } else {
            routines = routineRepository.findAll();
        }

        List<RoutineWorkout> createdRoutineWorkouts;

        // 5) For each routine add 5-8 RoutineWorkouts linking random workouts
        if (routineWorkoutRepository.count() == 0) {
            System.out.println("DataSeeder: creando RoutineWorkouts para rutinas...");

            createdRoutineWorkouts = new ArrayList<>();
            for (Routine r : routines) {
                int count = randomInt(5, 8);
                // shuffle and pick unique workouts
                List<Workout> chosen = new ArrayList<>(workouts);
                Collections.shuffle(chosen);
                chosen = chosen.subList(0, Math.min(count, chosen.size()));
                int pos = 1;
                for (Workout w : chosen) {
                    RoutineWorkout rw = RoutineWorkout.builder()
                            .routine(r)
                            .workout(w)
                            .position(pos++)
                            .sets(w.getSets() != null ? w.getSets() : 3)
                            .reps(String.valueOf(w.getReps() != null ? w.getReps() : 10))
                            .restSeconds(30)
                            .durationSeconds(w.getDurationSeconds())
                            .notes("Ejecutar con buena técnica")
                            .build();
                    createdRoutineWorkouts.add(routineWorkoutRepository.save(rw));
                }
            }
            System.out.println("Seed: created " + createdRoutineWorkouts.size() + " routine-workouts.");

        } else {

            createdRoutineWorkouts = routineWorkoutRepository.findAll();
            System.out.println("DataSeeder: RoutineWorkouts ya existen (" + createdRoutineWorkouts.size()
                    + " encontrados), saltando creación de RoutineWorkouts.");
        }

        // 6) Generate some UserWorkoutProgress entries for user id = 3 if session
        // repository exists
        try {
            // try to find or create a session for user id 3
            UserRoutineSession session = null;

            // First, create or find a UserRoutine
            UserRoutine userRoutine = userRoutineRepository
                    .findByUserIdAndRoutineId(author.getId(), routines.get(0).getId())
                    .orElseGet(() -> userRoutineRepository.save(
                            UserRoutine.builder()
                                    .user(author)
                                    .routine(routines.get(0))
                                    .status("active")
                                    .progressPercent(0)
                                    .build()));
            List<UserRoutineSession> sessions = userRoutineSessionRepository.findByUserRoutine(userRoutine);
            if (!sessions.isEmpty()) {
                session = sessions.get(0);
            } else {
                // create a new session
                UserRoutineSession s = UserRoutineSession.builder()
                        .userRoutine(userRoutine)
                        .sessionDate(java.time.LocalDateTime.now())
                        .durationSeconds(0)
                        .notes("Sesión creada por el seed")
                        .build();
                session = userRoutineSessionRepository.save(s);
            }

            List<UserWorkoutProgress> progresses = new ArrayList<>();
            // create progress for random routine-workouts
            if (userWorkoutProgressRepository.count() == 0) {
                for (int i = 0; i < Math.min(50, createdRoutineWorkouts.size()); i++) {

                    RoutineWorkout rw = createdRoutineWorkouts.get(randomInt(0, createdRoutineWorkouts.size() - 1));

                    UserWorkoutProgress p = UserWorkoutProgress.builder()
                            .session(session)
                            .routineWorkout(rw)
                            .workout(rw.getWorkout())
                            .setsCompleted(Math.max(1, rw.getSets() - randomInt(0, 1)))
                            .repsDetail(rw.getReps())
                            .weightUsed(0.0)
                            .durationSeconds(rw.getDurationSeconds() != null ? rw.getDurationSeconds() : 30)
                            .notes("Completado por el seed")
                            .build();

                    progresses.add(userWorkoutProgressRepository.save(p));

                }

                System.out.println("Seed: created " + progresses.size() + " user workout progress rows for user 3.");
            }
        } catch (Exception ex) {
            System.out.println(
                    "DataSeeder: No se pudo crear UserWorkoutProgress (UserRoutineSession repo/entity puede no existir). Error: "
                            + ex.getMessage());
            // no falla, sólo ignora si no existe la entidad/repo
        }

        System.out.println("DataSeeder: seed completo.");
    }

    // helpers
    private List<WorkoutCategory> ensureCategories() {
        if (categoryRepository.count() == 0) {
            List<WorkoutCategory> cats = Arrays.asList(
                    WorkoutCategory.builder().slug("strength").name("Strength").description("Fuerza").build(),
                    WorkoutCategory.builder().slug("cardio").name("Cardio").description("Cardio").build(),
                    WorkoutCategory.builder().slug("yoga").name("Yoga").description("Flexibilidad").build(),
                    WorkoutCategory.builder().slug("dance").name("Dance").description("Baile").build());
            return categoryRepository.saveAll(cats);
        } else {
            return categoryRepository.findAll();
        }
    }

    private static final Random R = ThreadLocalRandom.current();

    private int randomInt(int min, int max) {
        return R.nextInt(max - min + 1) + min;
    }

    private String randomDifficulty() {
        String[] d = { "beginner", "intermediate", "advanced" };
        return d[randomInt(0, d.length - 1)];
    }

    private String randomMuscleGroup() {
        String[] g = { "Full Body", "Legs", "Back", "Chest", "Shoulders", "Core" };
        return g[randomInt(0, g.length - 1)];
    }

    private String randomEquipment() {
        String[] e = { "None", "Dumbbells", "Barbell", "Kettlebell", "Resistance Bands" };
        return e[randomInt(0, e.length - 1)];
    }

    private String randomName() {
        String[] n = { "Power", "Burn", "Blast", "Flow", "Core", "Tone", "Rush", "Circuit" };
        return n[randomInt(0, n.length - 1)];
    }

    private String randomVideoType() {
        String[] t = { "tonificacion", "zumba", "cardio", "stretch", "hiit" };
        return t[randomInt(0, t.length - 1)];
    }

    private String randomResolution() {
        String[] r = { "720p", "1080p", "480p", "4K" };
        return r[randomInt(0, r.length - 1)];
    }

    private String randomRoutineName() {
        String[] r = { "Express", "Morning", "Evening", "Burn", "Focus", "Relax" };
        return r[randomInt(0, r.length - 1)];
    }

    private String randomRoutineThumbnail() {
        String[] t = {
                "https://picsum.photos/seed/routine1/600/400",
                "https://picsum.photos/seed/routine2/600/400",
                "https://picsum.photos/seed/routine3/600/400",
                "https://picsum.photos/seed/routine4/600/400",
                "https://picsum.photos/seed/routine5/600/400",
                "https://picsum.photos/seed/routine6/600/400",
                "https://picsum.photos/seed/routine7/600/400",
                "https://picsum.photos/seed/routine8/600/400",
                "https://picsum.photos/seed/routine9/600/400",
                "https://picsum.photos/seed/routine10/600/400"

        };
        return t[randomInt(0, t.length - 1)];
    }
}
