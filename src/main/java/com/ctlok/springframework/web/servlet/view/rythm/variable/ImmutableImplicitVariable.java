package com.ctlok.springframework.web.servlet.view.rythm.variable;

/**
 * @author Lawrence Cheung
 *
 */
public class ImmutableImplicitVariable implements ImplicitVariable {

    private final String name;
    private final String type;
    private final Object value;
    
    public ImmutableImplicitVariable(String name, String type, Object value) {
        super();
        this.name = name;
        this.type = type;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Object getValue() {
        return value;
    }

}
