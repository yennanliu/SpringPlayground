package EmployeeSystem.config;

// https://youtu.be/42MTtF44XAs?si=VnXx5TwPHOVeSZv5&t=53

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

// TODO : check how it works / if is necessary
@EnableR2dbcRepositories // enable r2dbc repo : similar as JPA
@Configuration
public class R2dbcConfig {}
