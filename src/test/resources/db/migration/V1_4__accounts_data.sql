INSERT INTO user(id, name, password)
      VALUES (1, 'victor', 'paco');

INSERT INTO bank_login(id, user_id, bank_provider)
      VALUES(1,1,'PTSB');

INSERT INTO bank_account(id, bank_login_id, account_number, name, alias)
  VALUES (1,1,'1234','test account','test account alias'),
         (2,1,'4321','test savings account','test savings account alias');
