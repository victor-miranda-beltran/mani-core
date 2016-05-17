INSERT INTO transaction(id, uid, bank_account_id,category_id, description_original, amount, date_authorization,date_settled, flow, status)
      VALUES (1, 'transuid1', 1, null, 'POS Netflix 01/10',10,'2016-10-1','2016-10-1','OUT','NORMAL'),
             (2, 'transuid2', 1, 18, 'POS Amazon.co.uk 01/12',10,'2016-12-1','2016-12-1','OUT','PENDING');