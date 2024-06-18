-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema quest
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `quest` ;

-- -----------------------------------------------------
-- Schema quest
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `quest` DEFAULT CHARACTER SET utf8 ;
SHOW WARNINGS;
USE `quest` ;

-- -----------------------------------------------------
-- Table `quest`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quest`.`role` (
                                              `value` VARCHAR(128) NOT NULL,
    PRIMARY KEY (`value`))
    ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `quest`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quest`.`users` (
                                               `user_id` INT NOT NULL AUTO_INCREMENT,
                                               `login` VARCHAR(128) NOT NULL,
    `name` VARCHAR(128) NULL,
    `password` VARCHAR(128) NULL,
    `role` VARCHAR(128) NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
    INDEX `fk_users_role_idx` (`role` ASC) VISIBLE,
    CONSTRAINT `fk_users_role`
    FOREIGN KEY (`role`)
    REFERENCES `quest`.`role` (`value`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
    ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `quest`.`quests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quest`.`quests` (
                                                `quest_id` INT NOT NULL AUTO_INCREMENT,
                                                `name` VARCHAR(512) NULL,
    `description` VARCHAR(2048) NULL,
    `first_question_id` INT NULL,
    `user_author_id` INT NULL,
    PRIMARY KEY (`quest_id`),
    INDEX `fk_quests_users1_idx` (`user_author_id` ASC) VISIBLE,
    CONSTRAINT `fk_quests_users`
    FOREIGN KEY (`user_author_id`)
    REFERENCES `quest`.`users` (`user_id`)
    ON DELETE SET NULL
    ON UPDATE SET NULL)
    ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `quest`.`game_state`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quest`.`game_state` (
                                                    `value` VARCHAR(128) NOT NULL,
    PRIMARY KEY (`value`))
    ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `quest`.`games`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quest`.`games` (
                                               `game_id` INT NOT NULL AUTO_INCREMENT,
                                               `current_question_id` INT NULL,
                                               `game_state` VARCHAR(128) NOT NULL,
    `quest_id` INT NULL,
    `user_player_id` INT NULL,
    `first_question_id` INT NULL,
    PRIMARY KEY (`game_id`),
    INDEX `fk_games_game_state1_idx` (`game_state` ASC) VISIBLE,
    INDEX `fk_games_quests1_idx` (`quest_id` ASC) VISIBLE,
    INDEX `fk_games_users2_idx` (`user_player_id` ASC) VISIBLE,
    CONSTRAINT `fk_games_game_state`
    FOREIGN KEY (`game_state`)
    REFERENCES `quest`.`game_state` (`value`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_games_quests`
    FOREIGN KEY (`quest_id`)
    REFERENCES `quest`.`quests` (`quest_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT `fk_games_users`
    FOREIGN KEY (`user_player_id`)
    REFERENCES `quest`.`users` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `quest`.`questions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quest`.`questions` (
                                                   `question_id` INT NOT NULL AUTO_INCREMENT,
                                                   `question_message` VARCHAR(512) NOT NULL,
    `quest_id` INT NOT NULL,
    PRIMARY KEY (`question_id`),
    INDEX `fk_questions_quests1_idx` (`quest_id` ASC) VISIBLE,
    CONSTRAINT `fk_questions_quests`
    FOREIGN KEY (`quest_id`)
    REFERENCES `quest`.`quests` (`quest_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `quest`.`answers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quest`.`answers` (
                                                 `answer_id` INT NOT NULL AUTO_INCREMENT,
                                                 `answer_message` VARCHAR(1024) NOT NULL,
    `final_message` VARCHAR(1024) NULL,
    `next_question_id` INT NULL,
    `game_state` VARCHAR(128) NOT NULL,
    `question_id` INT NOT NULL,
    PRIMARY KEY (`answer_id`),
    INDEX `fk_answers_game_state1_idx` (`game_state` ASC) VISIBLE,
    INDEX `fk_answers_questions1_idx` (`question_id` ASC) VISIBLE,
    CONSTRAINT `fk_answers_game_state`
    FOREIGN KEY (`game_state`)
    REFERENCES `quest`.`game_state` (`value`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_answers_questions`
    FOREIGN KEY (`question_id`)
    REFERENCES `quest`.`questions` (`question_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `quest`.`role`
-- -----------------------------------------------------
START TRANSACTION;
USE `quest`;
INSERT INTO `quest`.`role` (`value`) VALUES ('ADMIN');
INSERT INTO `quest`.`role` (`value`) VALUES ('THE_USER');
INSERT INTO `quest`.`role` (`value`) VALUES ('GUEST');

COMMIT;


-- -----------------------------------------------------
-- Data for table `quest`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `quest`;
INSERT INTO `quest`.`users` (`user_id`, `login`, `name`, `password`, `role`) VALUES (DEFAULT, 'admin', 'Admin', 'admin', 'ADMIN');
INSERT INTO `quest`.`users` (`user_id`, `login`, `name`, `password`, `role`) VALUES (DEFAULT, 'anonymous', 'Anonymous', 'anonymous-anonymous', 'GUEST');
INSERT INTO `quest`.`users` (`user_id`, `login`, `name`, `password`, `role`) VALUES (DEFAULT, 'olga', 'Olga', '12345qw!@', 'ADMIN');
INSERT INTO `quest`.`users` (`user_id`, `login`, `name`, `password`, `role`) VALUES (DEFAULT, 'meduza', 'Meduza', '12345qw!@', 'THE_USER');

COMMIT;


-- -----------------------------------------------------
-- Data for table `quest`.`game_state`
-- -----------------------------------------------------
START TRANSACTION;
USE `quest`;
INSERT INTO `quest`.`game_state` (`value`) VALUES ('GAME');
INSERT INTO `quest`.`game_state` (`value`) VALUES ('WIN');
INSERT INTO `quest`.`game_state` (`value`) VALUES ('LOSE');

COMMIT;

