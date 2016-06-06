package com.victormiranda.mani.core.controller;

import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.core.service.synchronization.SynchronizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SynchronizationController {

	private final SynchronizationService synchronizationService;

	@Autowired
	public SynchronizationController(SynchronizationService synchronizationService) {
		this.synchronizationService = synchronizationService;
	}

	@RequestMapping(value = "/sync/{bankLoginId}", method = RequestMethod.POST)
	public SynchronizationResult sync(@PathVariable final Integer bankLoginId) {
		return synchronizationService.sync(bankLoginId);
	}

}
