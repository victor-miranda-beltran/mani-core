package com.victormiranda.mani.core.service.synchronization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Credentials;
import com.victormiranda.mani.bean.SynchronizationRequest;
import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.core.dao.bankaccount.BankLoginDao;
import com.victormiranda.mani.core.model.BankLogin;
import com.victormiranda.mani.core.service.bankaccount.BankAccountService;
import com.victormiranda.mani.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class SynchronizationServiceImpl implements SynchronizationService {

	private final ObjectMapper objectMapper;
	private final String bankScrapperURL;
	private final RestOperations restTemplate;
	private final UserService userService;
	private final BankLoginDao bankLoginDao;
	private final BankAccountService bankAccountService;

	@Autowired
	public SynchronizationServiceImpl(
			final ObjectMapper objectMapper,
			final @Value("${services.bankscrapper.url}") String bankScrapperURL,
			final UserService userService,
			final BankLoginDao bankLoginDao,
			final BankAccountService bankAccountService) {
		this.objectMapper = objectMapper;
		this.bankScrapperURL = bankScrapperURL;
		this.restTemplate = new RestTemplate();
		this.userService = userService;
		this.bankLoginDao = bankLoginDao;
		this.bankAccountService = bankAccountService;
	}

	@Override
	public SynchronizationResult sync(final Integer bankLoginId) {
		final BankLogin bankLogin = bankLoginDao.findOne(bankLoginId);

		final Credentials credentials = bankAccountService.getLoginCredentials(bankLogin);

		final Set<AccountInfo> accountInfoSet = bankAccountService.getAccountsInfoByUserId(
				userService.getCurrentUserId().get());

		final SynchronizationRequest synchronizationRequest =
				new SynchronizationRequest(credentials, accountInfoSet);

		final SynchronizationResult synchronizationResult =
				restTemplate.postForObject(bankScrapperURL + "/scrape",synchronizationRequest, SynchronizationResult.class);

		bankAccountService.updateBankAccounts(bankLogin, synchronizationResult);

		return synchronizationResult;
	}

}
