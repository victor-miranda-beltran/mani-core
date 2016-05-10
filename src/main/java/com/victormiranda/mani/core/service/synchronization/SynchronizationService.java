package com.victormiranda.mani.core.service.synchronization;

import com.victormiranda.mani.bean.SynchronizationResult;

public interface SynchronizationService {

	SynchronizationResult sync(final Integer bankLoginId);

}
