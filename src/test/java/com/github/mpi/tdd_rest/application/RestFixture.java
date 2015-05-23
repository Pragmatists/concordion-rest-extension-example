package com.github.mpi.tdd_rest.application;

import org.concordion.api.extension.Extension;
import org.junit.runner.RunWith;

import pl.pragmatists.concordion.rest.RestExtension;

import com.github.mpi.support.RestRunner;

@RunWith(RestRunner.class)
public abstract class RestFixture {

    @Extension
    public RestExtension rest = new RestExtension()
                                        .enableCodeMirror()
                                        .includeBoostrap();
    
}
