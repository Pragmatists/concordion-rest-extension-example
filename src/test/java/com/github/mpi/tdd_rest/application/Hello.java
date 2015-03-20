package com.github.mpi.tdd_rest.application;

import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import rest.fixtures.RestFixture;

import com.github.mpi.Application;
import com.github.mpi.support.RestRunner;

@RunWith(RestRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@IntegrationTest
@WebAppConfiguration
public class Hello extends RestFixture {
    
}
