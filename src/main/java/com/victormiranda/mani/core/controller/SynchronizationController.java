package com.victormiranda.mani.core.controller;

import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.core.service.synchronization.SynchronizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SynchronizationController {

	private final SynchronizationService synchronizationService;

	@Autowired
	public SynchronizationController(SynchronizationService synchronizationService) {
		this.synchronizationService = synchronizationService;
	}

	@RequestMapping("/sync")
	public SynchronizationResult sync() {
		final Integer bankLoginId = 1;
		return synchronizationService.sync(bankLoginId);
	}

}
