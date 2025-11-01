-- V1__init.sql (compatible con MySQL < 5.7 usando LONGTEXT en lugar de JSON)
USE sql10800933;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE roles (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE users (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  display_name VARCHAR(120),
  role_id INT UNSIGNED NOT NULL,
  date_of_birth DATE NOT NULL,
  sex VARCHAR(16) NOT NULL,
  height_cm SMALLINT UNSIGNED,
  weight_kg DECIMAL(6,2),
  metadata LONGTEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP,
  CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB;

CREATE TABLE workout_categories (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  slug VARCHAR(60) NOT NULL UNIQUE,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE workouts (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  category_id SMALLINT UNSIGNED,
  difficulty VARCHAR(20) DEFAULT 'beginner',
  main_muscle_group VARCHAR(100),
  equipment VARCHAR(100),
  thumbnail_url VARCHAR(512),
  metadata LONGTEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT fk_workouts_category FOREIGN KEY (category_id) REFERENCES workout_categories(id)
) ENGINE=InnoDB;

CREATE TABLE workout_videos (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  workout_id BIGINT UNSIGNED,
  title VARCHAR(255) NOT NULL,
  url VARCHAR(1024) NOT NULL,
  duration_seconds INT,
  video_type VARCHAR(50) NOT NULL DEFAULT 'otro',
  resolution VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_videos_workout FOREIGN KEY (workout_id) REFERENCES workouts(id)
) ENGINE=InnoDB;

CREATE TABLE routines (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  difficulty VARCHAR(20),
  duration_minutes SMALLINT UNSIGNED,
  author_user_id BIGINT UNSIGNED,
  is_public TINYINT(1) DEFAULT 1,
  metadata LONGTEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT fk_routine_author FOREIGN KEY (author_user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE routine_workouts (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  routine_id BIGINT UNSIGNED NOT NULL,
  workout_id BIGINT UNSIGNED NOT NULL,
  position SMALLINT UNSIGNED NOT NULL,
  sets SMALLINT UNSIGNED,
  reps VARCHAR(50),
  rest_seconds INT,
  duration_seconds INT,
  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_re_routine FOREIGN KEY (routine_id) REFERENCES routines(id) ON DELETE CASCADE,
  CONSTRAINT fk_re_workout FOREIGN KEY (workout_id) REFERENCES workouts(id),
  UNIQUE KEY ux_routine_position (routine_id, position)
) ENGINE=InnoDB;

CREATE TABLE user_routines (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT UNSIGNED NOT NULL,
  routine_id BIGINT UNSIGNED NOT NULL,
  start_date DATE,
  end_date DATE,
  progress_percent TINYINT UNSIGNED DEFAULT 0,
  status VARCHAR(20) DEFAULT 'active',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_ur_routine FOREIGN KEY (routine_id) REFERENCES routines(id),
  INDEX idx_user_status (user_id, status)
) ENGINE=InnoDB;

CREATE TABLE user_routine_sessions (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_routine_id BIGINT UNSIGNED NOT NULL,
  session_date DATETIME,
  duration_seconds INT,
  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_urs_user_routine FOREIGN KEY (user_routine_id) REFERENCES user_routines(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE user_workout_progress (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  session_id BIGINT UNSIGNED NOT NULL,
  routine_workout_id BIGINT UNSIGNED,
  workout_id BIGINT UNSIGNED NOT NULL,
  sets_completed SMALLINT UNSIGNED,
  reps_detail VARCHAR(255),
  weight_used DECIMAL(8,2),
  duration_seconds INT,
  notes TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_uep_session FOREIGN KEY (session_id) REFERENCES user_routine_sessions(id) ON DELETE CASCADE,
  CONSTRAINT fk_uep_routine_workout FOREIGN KEY (routine_workout_id) REFERENCES routine_workouts(id),
  CONSTRAINT fk_uep_workout FOREIGN KEY (workout_id) REFERENCES workouts(id),
  INDEX idx_session (session_id)
) ENGINE=InnoDB;

CREATE TABLE streaks (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT UNSIGNED NOT NULL,
  name VARCHAR(100) DEFAULT 'default_streak',
  current_streak INT UNSIGNED DEFAULT 0,
  longest_streak INT UNSIGNED DEFAULT 0,
  last_date DATE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT fk_streak_user FOREIGN KEY (user_id) REFERENCES users(id),
  UNIQUE (user_id, name)
) ENGINE=InnoDB;

CREATE TABLE rewards (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(80) NOT NULL UNIQUE,
  title VARCHAR(255),
  description TEXT,
  reward_type VARCHAR(50),
  metadata LONGTEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE reward_conditions (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  reward_id BIGINT UNSIGNED NOT NULL,
  condition_type VARCHAR(100),
  condition_value VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_condition_reward FOREIGN KEY (reward_id) REFERENCES rewards(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE user_rewards (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT UNSIGNED NOT NULL,
  reward_id BIGINT UNSIGNED NOT NULL,
  awarded_at DATETIME,
  redeemed_at DATETIME,
  metadata LONGTEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_user_reward_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_user_reward_reward FOREIGN KEY (reward_id) REFERENCES rewards(id),
  INDEX idx_user_reward (user_id, reward_id)
) ENGINE=InnoDB;

CREATE TABLE audit_logs (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT UNSIGNED,
  action VARCHAR(150) NOT NULL,
  resource_type VARCHAR(80),
  resource_id VARCHAR(80),
  details LONGTEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  INDEX idx_user_action (user_id, action),
  CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;
