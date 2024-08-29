package group.phenikaa.hotelmanager.impl.convert;

public abstract class Converter<F, T> {
    protected abstract T doForward(F input);
    protected abstract F doBackward(T input);

    public T convert(F input) {
        return doForward(input);
    }

    public F reverseConvert(T input) {
        return doBackward(input);
    }
}

