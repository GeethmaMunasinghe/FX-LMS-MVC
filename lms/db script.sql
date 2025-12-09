-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema nextstackMvc
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema nextstackMvc
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `nextstackMvc` DEFAULT CHARACTER SET utf8 ;
USE `nextstackMvc` ;

-- -----------------------------------------------------
-- Table `nextstackMvc`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nextstackMvc`.`user` (
  `email` VARCHAR(100) NOT NULL,
  `full_name` VARCHAR(150) NULL,
  `age` INT NULL,
  `password` VARCHAR(750) NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `nextstackMvc`.`student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nextstackMvc`.`student` (
  `id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(100) NULL,
  `address` VARCHAR(100) NULL,
  `dob` DATE NULL,
  `user_email` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_student_user_idx` (`user_email` ASC) VISIBLE,
  CONSTRAINT `fk_student_user`
    FOREIGN KEY (`user_email`)
    REFERENCES `nextstackMvc`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `nextstackMvc`.`teacher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nextstackMvc`.`teacher` (
  `id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(100) NULL,
  `contact` VARCHAR(45) NULL,
  `address` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `nextstackMvc`.`module`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nextstackMvc`.`module` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `nextstackMvc`.`program`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nextstackMvc`.`program` (
  `id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NULL,
  `cost` DOUBLE NULL,
  `teacher_id` VARCHAR(45) NOT NULL,
  `module_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_program_teacher1_idx` (`teacher_id` ASC) VISIBLE,
  INDEX `fk_program_module1_idx` (`module_id` ASC) VISIBLE,
  CONSTRAINT `fk_program_teacher1`
    FOREIGN KEY (`teacher_id`)
    REFERENCES `nextstackMvc`.`teacher` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_program_module1`
    FOREIGN KEY (`module_id`)
    REFERENCES `nextstackMvc`.`module` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `nextstackMvc`.`intake`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nextstackMvc`.`intake` (
  `id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(100) NULL,
  `date` DATE NULL,
  `program_id` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_intake_program1_idx` (`program_id` ASC) VISIBLE,
  CONSTRAINT `fk_intake_program1`
    FOREIGN KEY (`program_id`)
    REFERENCES `nextstackMvc`.`program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `nextstackMvc`.`enroll`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nextstackMvc`.`enroll` (
  `program_id` VARCHAR(45) NOT NULL,
  `student_id` VARCHAR(45) NOT NULL,
  `isPaid` TINYINT NULL,
  PRIMARY KEY (`program_id`, `student_id`),
  INDEX `fk_program_has_student_student1_idx` (`student_id` ASC) VISIBLE,
  INDEX `fk_program_has_student_program1_idx` (`program_id` ASC) VISIBLE,
  CONSTRAINT `fk_program_has_student_program1`
    FOREIGN KEY (`program_id`)
    REFERENCES `nextstackMvc`.`program` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_program_has_student_student1`
    FOREIGN KEY (`student_id`)
    REFERENCES `nextstackMvc`.`student` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
