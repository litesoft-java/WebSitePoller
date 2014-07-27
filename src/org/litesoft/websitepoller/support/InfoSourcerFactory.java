package org.litesoft.websitepoller.support;

public interface InfoSourcerFactory {
    public class Pair {
        public InfoSource source;
        public InfoSourcer sourcer;
    }

    Pair create();
}
