package com.victormiranda.mani.core.service.synchronization;

import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.core.model.BankLogin;

public interface SynchronizationService {

	SynchronizationResult sync(final Integer bankLoginId);
}
