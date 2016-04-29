package com.victormiranda.mani.core.service.synchronization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Credentials;
import com.victormiranda.mani.bean.SynchronizationRequest;
import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.ptsb.PTSBCredentials;
import com.victormiranda.mani.core.dao.bankaccount.BankLoginDao;
import com.victormiranda.mani.core.model.BankLogin;
import com.victormiranda.mani.core.model.User;
import com.victormiranda.mani.core.service.bankaccount.BankAccountService;
import com.victormiranda.mani.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class SynchronizationServiceImpl implements SynchronizationService {

	private final ObjectMapper objectMapper;
	private final String bankScrapperURL;
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
		this.userService = userService;
		this.bankLoginDao = bankLoginDao;
		this.bankAccountService = bankAccountService;
	}

	@Override
	public SynchronizationResult sync(final Integer bankLoginId) {
		final BankLogin bankLogin = bankLoginDao.findOne(bankLoginId);

		final Credentials credentials = bankAccountService.getLoginCredentials(bankLogin);
		final Set<AccountInfo> accountInfoSet = bankAccountService.getAccountsInfo(bankLogin);

		final SynchronizationRequest synchronizationRequest =
				new SynchronizationRequest(credentials, accountInfoSet);

		final SynchronizationResult synchronizationResult = mockedSResult();

		bankAccountService.updateBankAccounts(bankLogin, synchronizationResult);

		return synchronizationResult;
	}

	private SynchronizationResult mockedSResult() {
		SynchronizationResult synchronizationResult = null;
		try {
			final String s = "{"+
					"  \"accounts\": [\n" +
							"    {\n" +
							"      \"id\": \"permanenttsb Current - 7931 permanenttsb Current - 7931 permanenttsb Current - 7931 permanenttsb Current - 7931 permanenttsb Current 7931\",\n" +
							"      \"uid\": \"95283370-141f-4001-952f-ed43b6deb71d\",\n" +
							"      \"availableBalance\": 3994.85,\n" +
							"      \"currentBalance\": 4069.73,\n" +
							"      \"lastSynced\": [\n" +
							"        2016,\n" +
							"        4,\n" +
							"        27\n" +
							"      ],\n" +
							"      \"transactions\": [\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793127/04/2016 00:00:00CNC TESCO STORES 25/04 121.25004069.73\",\n" +
							"          \"description\": \"CNC TESCO STORES 25/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            25\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 21.25,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793126/04/2016 00:00:00SANDRA SANCHEZ    VICTOR100.00004090.98\",\n" +
							"          \"description\": \"SANDRA SANCHEZ VICTOR\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            26\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 100,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793126/04/2016 00:00:00CNC CARRYOUT CLO 24/04 013.30004190.98\",\n" +
							"          \"description\": \"CNC CARRYOUT CLO 24/04 0\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            24\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 13.3,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793126/04/2016 00:00:00POS RAHENY VETIN 23/04 152.00004204.28\",\n" +
							"          \"description\": \"POS RAHENY VETIN 23/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            23\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 52,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793126/04/2016 00:00:00CNC LIDL 0942 BA 23/04 120.48004256.28\",\n" +
							"          \"description\": \"CNC LIDL 0942 BA 23/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            23\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 20.48,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793126/04/2016 00:00:00POS Hailo Networ 23/04 16.80004276.76\",\n" +
							"          \"description\": \"POS Hailo Networ 23/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            23\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 6.8,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793126/04/2016 00:00:00POS WOODIES DIY 23/04 1569.94004283.56\",\n" +
							"          \"description\": \"POS WOODIES DIY 23/04 15\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            23\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 69.94,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793126/04/2016 00:00:00POS PAYPAL *PETT 23/04 115.00004353.50\",\n" +
							"          \"description\": \"POS PAYPAL *PETT 23/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            23\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 15,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793126/04/2016 00:00:00POS Hailo Networ 23/04 17.20004368.50\",\n" +
							"          \"description\": \"POS Hailo Networ 23/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            23\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 7.2,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793126/04/2016 00:00:00POS CARRYOUT CLO 22/04 114.47004375.70\",\n" +
							"          \"description\": \"POS CARRYOUT CLO 22/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            26\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 14.47,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793126/04/2016 00:00:00CNC DUNNES DONAG 22/04 09.50004390.17\",\n" +
							"          \"description\": \"CNC DUNNES DONAG 22/04 0\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            26\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 9.5,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793125/04/2016 00:00:00POS GREYHOUND RE 21/04 117.00004399.67\",\n" +
							"          \"description\": \"POS GREYHOUND RE 21/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            25\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 17,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793125/04/2016 00:00:00CNC TESCO STORES 21/04 06.05004416.67\",\n" +
							"          \"description\": \"CNC TESCO STORES 21/04 0\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            25\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 6.05,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793122/04/2016 00:00:00INET 24 Fr 4162400.00004422.72\",\n" +
							"          \"description\": \"INET 24 Fr 4162\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            22\n" +
							"          ],\n" +
							"          \"flow\": \"IN\",\n" +
							"          \"amount\": 400,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793121/04/2016 00:00:00CNC TESCO STORES 19/04 013.83004022.72\",\n" +
							"          \"description\": \"CNC TESCO STORES 19/04 0\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            19\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 13.83,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793120/04/2016 00:00:00POS TESCO STORES 18/04 19.44004036.55\",\n" +
							"          \"description\": \"POS TESCO STORES 18/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            18\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 9.44,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793120/04/2016 00:00:00POS TV LICENSE O 15/04 1160.00004045.99\",\n" +
							"          \"description\": \"POS TV LICENSE O 15/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            20\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 160,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793119/04/2016 00:00:00CNC CARRYOUT CLO 17/04 014.47004205.99\",\n" +
							"          \"description\": \"CNC CARRYOUT CLO 17/04 0\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            17\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 14.47,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793119/04/2016 00:00:00CNC CARRYOUT CLO 16/04 05.50004220.46\",\n" +
							"          \"description\": \"CNC CARRYOUT CLO 16/04 0\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            16\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 5.5,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793119/04/2016 00:00:00POS CENTRA 16/04 11:26 95.90004225.96\",\n" +
							"          \"description\": \"POS CENTRA 16/04 11:26 9\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            19\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 5.9,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793119/04/2016 00:00:00CNC LIDL 0942 BA 16/04 128.14004231.86\",\n" +
							"          \"description\": \"CNC LIDL 0942 BA 16/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            16\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 28.14,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793118/04/2016 00:00:00INET 24 Fr 69562000.00004260.00\",\n" +
							"          \"description\": \"INET 24 Fr 6956\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            18\n" +
							"          ],\n" +
							"          \"flow\": \"IN\",\n" +
							"          \"amount\": 2000,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793118/04/2016 00:00:00INET 24 Fr 4162160.00002260.00\",\n" +
							"          \"description\": \"INET 24 Fr 4162\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            18\n" +
							"          ],\n" +
							"          \"flow\": \"IN\",\n" +
							"          \"amount\": 160,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current793115/04/2016 00:00:00INET 24 Fr 41622100.00002100.00\",\n" +
							"          \"description\": \"INET 24 Fr 4162\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            15\n" +
							"          ],\n" +
							"          \"flow\": \"IN\",\n" +
							"          \"amount\": 2100,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        }\n" +
							"      ]\n" +
							"    },\n" +
							"    {\n" +
							"      \"id\": \"40 Day Notice - 4008 40 Day Notice - 4008 40 Day Notice - 4008 40 Day Notice - 4008\",\n" +
							"      \"uid\": \"414a3e42-f8fc-4e89-8356-1559c2e1a7c7\",\n" +
							"      \"availableBalance\": 7500,\n" +
							"      \"currentBalance\": 7500,\n" +
							"      \"lastSynced\": [\n" +
							"        2016,\n" +
							"        4,\n" +
							"        27\n" +
							"      ],\n" +
							"      \"transactions\": [\n" +
							"        {\n" +
							"          \"transactionUID\": \"40 Day Notice400818/04/2016 00:00:00INET 24 Fr 69562500.00007500.00\",\n" +
							"          \"description\": \"INET 24 Fr 6956\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            18\n" +
							"          ],\n" +
							"          \"flow\": \"IN\",\n" +
							"          \"amount\": 2500,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"40 Day Notice400811/04/2016 00:00:00INET 24 Fr 69565000.00005000.00\",\n" +
							"          \"description\": \"INET 24 Fr 6956\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            11\n" +
							"          ],\n" +
							"          \"flow\": \"IN\",\n" +
							"          \"amount\": 5000,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        }\n" +
							"      ]\n" +
							"    },\n" +
							"    {\n" +
							"      \"id\": \"permanenttsb Current - 6956 permanenttsb Current - 6956 permanenttsb Current - 6956 permanenttsb Current - 6956 permanenttsb Current 6956\",\n" +
							"      \"uid\": \"3479c618-9c6c-472f-bd1f-6bbb8b52c016\",\n" +
							"      \"availableBalance\": 3592.91,\n" +
							"      \"currentBalance\": 3592.91,\n" +
							"      \"lastSynced\": [\n" +
							"        2016,\n" +
							"        4,\n" +
							"        27\n" +
							"      ],\n" +
							"      \"transactions\": [\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695627/04/2016 00:00:00CT School Things Ltd t/a3554.625819111631ICTFR3592.91\",\n" +
							"          \"description\": \"CT School Things Ltd t/a\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            27\n" +
							"          ],\n" +
							"          \"flow\": \"IN\",\n" +
							"          \"amount\": 3554.62,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695626/04/2016 00:00:00POS AMAZON.UK PA 22/04 1230.070038.29\",\n" +
							"          \"description\": \"POS AMAZON.UK PA 22/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            26\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 230.07,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695625/04/2016 00:00:00POS AMAZON.ES CO 21/04 1290.8300268.36\",\n" +
							"          \"description\": \"POS AMAZON.ES CO 21/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            25\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 290.83,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695625/04/2016 00:00:00POS AMAZON.UK PA 21/04 2142.3000559.19\",\n" +
							"          \"description\": \"POS AMAZON.UK PA 21/04 2\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            25\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 142.3,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695618/04/2016 00:00:00MobileTopUp08XXXX793910.0000701.49\",\n" +
							"          \"description\": \"MobileTopUp08XXXX7939\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            18\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 10,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695618/04/2016 00:00:00INET 24 To 40082500.0000711.49\",\n" +
							"          \"description\": \"INET 24 To 4008\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            18\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 2500,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695618/04/2016 00:00:00INET 24 To 79312000.00003211.49\",\n" +
							"          \"description\": \"INET 24 To 7931\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            18\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 2000,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695613/04/2016 00:00:00POS FOUR STAR PI 11/04 121.50005211.49\",\n" +
							"          \"description\": \"POS FOUR STAR PI 11/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            11\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 21.5,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695612/04/2016 00:00:00POS PAYPAL *SPAN 09/04 151.65005232.99\",\n" +
							"          \"description\": \"POS PAYPAL *SPAN 09/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            9\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 51.65,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695611/04/2016 00:00:00SHEILA FERNANDEZ  RENT100.00005284.64\",\n" +
							"          \"description\": \"SHEILA FERNANDEZ RENT\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            11\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 100,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695611/04/2016 00:00:00SHEILA FERNANDEZ  RENT1000.00005384.64\",\n" +
							"          \"description\": \"SHEILA FERNANDEZ RENT\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            11\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 1000,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695611/04/2016 00:00:00INET 24 To 40085000.00006384.64\",\n" +
							"          \"description\": \"INET 24 To 4008\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            11\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 5000,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695611/04/2016 00:00:00INET 24 To 81751000.000011384.64\",\n" +
							"          \"description\": \"INET 24 To 8175\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            11\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 1000,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695607/04/2016 00:00:00SHEILA FERNANDEZ  RENT100.000012384.64\",\n" +
							"          \"description\": \"SHEILA FERNANDEZ RENT\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            7\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 100,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695607/04/2016 00:00:00POS FOUR STAR PI 05/04 121.500012484.64\",\n" +
							"          \"description\": \"POS FOUR STAR PI 05/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            5\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 21.5,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695607/04/2016 00:00:00POS WWW MAGNET I 05/04 150.000012506.14\",\n" +
							"          \"description\": \"POS WWW MAGNET I 05/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            5\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 50,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695605/04/2016 00:00:00Debit Card Charge0.460012556.14\",\n" +
							"          \"description\": \"Debit Card Charge\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            5\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 0.46,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695605/04/2016 00:00:00GBP/6.99/0.7898300000\",\n" +
							"          \"description\": \"GBP/6.99/0.789830\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            5\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 0,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695605/04/2016 00:00:00POS NOWTV.COM/BI 02/04 18.850012556.60\",\n" +
							"          \"description\": \"POS NOWTV.COM/BI 02/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            2\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 8.85,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695605/04/2016 00:00:00Debit Card Charge2.890012565.45\",\n" +
							"          \"description\": \"Debit Card Charge\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            5\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 2.89,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695605/04/2016 00:00:00GBP/130.77/0.7901980000\",\n" +
							"          \"description\": \"GBP/130.77/0.790198\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            5\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 0,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695605/04/2016 00:00:00POS Amazon UK Re 02/04 1165.490012568.34\",\n" +
							"          \"description\": \"POS Amazon UK Re 02/04 1\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            2\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 165.49,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695605/04/2016 00:00:00POS PAYPAL *NETF 02/04 08.990012733.83\",\n" +
							"          \"description\": \"POS PAYPAL *NETF 02/04 0\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            2\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 8.99,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695605/04/2016 00:00:00POS CUSTOM HOUSE 01/04 080.000012742.82\",\n" +
							"          \"description\": \"POS CUSTOM HOUSE 01/04 0\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            5\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 80,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695601/04/2016 00:00:00CT SCHOOL THING LTD3554.635816534833ICTFR12822.82\",\n" +
							"          \"description\": \"CT SCHOOL THING LTD\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            1\n" +
							"          ],\n" +
							"          \"flow\": \"IN\",\n" +
							"          \"amount\": 3554.63,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        },\n" +
							"        {\n" +
							"          \"transactionUID\": \"permanenttsb Current695630/03/2016 00:00:00DD LEAP AUTOLOAD - NTA30.00581578724IDSDD9268.19\",\n" +
							"          \"description\": \"DD LEAP AUTOLOAD - NTA\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            3,\n" +
							"            30\n" +
							"          ],\n" +
							"          \"flow\": \"OUT\",\n" +
							"          \"amount\": 30,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        }\n" +
							"      ]\n" +
							"    },\n" +
							"    {\n" +
							"      \"id\": \"vsaving - 8175 vsaving - 8175 vsaving - 8175 vsaving - 8175 vsaving 8175\",\n" +
							"      \"uid\": \"e4c4c469-3ea7-4efe-be90-fdf7dbcdb294\",\n" +
							"      \"availableBalance\": 1000,\n" +
							"      \"currentBalance\": 1000,\n" +
							"      \"lastSynced\": [\n" +
							"        2016,\n" +
							"        4,\n" +
							"        27\n" +
							"      ],\n" +
							"      \"transactions\": [\n" +
							"        {\n" +
							"          \"transactionUID\": \"vsaving817511/04/2016 00:00:00INET 24 Fr 69561000.00001000.00\",\n" +
							"          \"description\": \"INET 24 Fr 6956\",\n" +
							"          \"date\": [\n" +
							"            2016,\n" +
							"            4,\n" +
							"            11\n" +
							"          ],\n" +
							"          \"flow\": \"IN\",\n" +
							"          \"amount\": 1000,\n" +
							"          \"status\": \"NORMAL\"\n" +
							"        }\n" +
							"      ]\n" +
							"    }\n" +
							"  ],\n" +
							"  \"syncDone\": true\n" +
							"}";
			synchronizationResult = objectMapper.readValue(s, SynchronizationResult.class);
return synchronizationResult;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
