package org.litesoft.websitepoller.support;

import java.util.*;

public interface SourceTargetFactory {
    StartableSourceTarget create( String pName, List<InfoSource> pSources );
}
