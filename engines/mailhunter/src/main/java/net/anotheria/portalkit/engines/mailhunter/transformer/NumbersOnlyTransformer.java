package net.anotheria.portalkit.engines.mailhunter.transformer;

public class NumbersOnlyTransformer extends AbstractTransformer {

    @Override
    public String transform(String s) {

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || c == ' ')
                ret.append(c);
        }
        return ret.toString();
    }
}
