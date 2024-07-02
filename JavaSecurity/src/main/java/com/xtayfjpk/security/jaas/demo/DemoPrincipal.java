package com.xtayfjpk.security.jaas.demo;

import java.security.Principal;

public class DemoPrincipal implements Principal {
    private String name;

    public DemoPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "DemoPrincipal{" +
                "name='" + name + '\'' +
                '}';
    }
}
