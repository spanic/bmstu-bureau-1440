-- Active: 1769037196823@@localhost@5432@hw_1_test

/*                                       
                     $$\                         
                     $$ |                        
 $$$$$$$\  $$$$$$\ $$$$$$\   $$\   $$\  $$$$$$\  
$$  _____|$$  __$$\\_$$  _|  $$ |  $$ |$$  __$$\ 
\$$$$$$\  $$$$$$$$ | $$ |    $$ |  $$ |$$ /  $$ |
 \____$$\ $$   ____| $$ |$$\ $$ |  $$ |$$ |  $$ |
$$$$$$$  |\$$$$$$$\  \$$$$  |\$$$$$$  |$$$$$$$  |
\_______/  \_______|  \____/  \______/ $$  ____/ 
                                       $$ |      
                                       $$ |      
                                       \__|
**/

CREATE TABLE clients (
    client_id SERIAL PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    phone VARCHAR(30) UNIQUE,
    email VARCHAR(120) UNIQUE,
    created_at DATE NOT NULL
);

CREATE TABLE accounts (
    account_id SERIAL PRIMARY KEY,
    client_id INT NOT NULL REFERENCES clients (client_id),
    account_type VARCHAR(20) NOT NULL CHECK (
        account_type IN ('debit', 'savings', 'credit')
    ),
    currency CHAR(3) NOT NULL,
    opened_at DATE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (
        status IN ('active', 'blocked', 'closed')
    )
);

CREATE TABLE cards (
    card_id SERIAL PRIMARY KEY,
    account_id INT NOT NULL REFERENCES accounts (account_id),
    card_type VARCHAR(20) NOT NULL CHECK (
        card_type IN ('debit', 'credit', 'virtual')
    ),
    issued_at DATE NOT NULL,
    expires_at DATE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (
        status IN (
            'active',
            'blocked',
            'expired'
        )
    )
);

CREATE TABLE transactions (
    transaction_id SERIAL PRIMARY KEY,
    account_id INT NOT NULL REFERENCES accounts (account_id),
    txn_type VARCHAR(20) NOT NULL CHECK (
        txn_type IN (
            'deposit',
            'withdrawal',
            'transfer_in',
            'transfer_out',
            'fee'
        )
    ),
    amount NUMERIC(14, 2) NOT NULL CHECK (amount >= 0),
    txn_date TIMESTAMP NOT NULL,
    description VARCHAR(200)
);

CREATE TABLE loans (
    loan_id SERIAL PRIMARY KEY,
    client_id INT NOT NULL REFERENCES clients (client_id),
    principal NUMERIC(14, 2) NOT NULL CHECK (principal > 0),
    interest_rate NUMERIC(5, 2) NOT NULL CHECK (interest_rate >= 0),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (
        status IN ('active', 'closed', 'overdue')
    )
);

CREATE TABLE loan_payments (
    payment_id SERIAL PRIMARY KEY,
    loan_id INT NOT NULL REFERENCES loans (loan_id),
    amount NUMERIC(14, 2) NOT NULL CHECK (amount > 0),
    payment_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (
        status IN ('success', 'failed')
    )
);

/*
          $$\ $$\                      $$\               
          $$ |\__|                     $$ |              
 $$$$$$$\ $$ |$$\  $$$$$$\  $$$$$$$\ $$$$$$\    $$$$$$$\ 
$$  _____|$$ |$$ |$$  __$$\ $$  __$$\\_$$  _|  $$  _____|
$$ /      $$ |$$ |$$$$$$$$ |$$ |  $$ | $$ |    \$$$$$$\  
$$ |      $$ |$$ |$$   ____|$$ |  $$ | $$ |$$\  \____$$\ 
\$$$$$$$\ $$ |$$ |\$$$$$$$\ $$ |  $$ | \$$$$  |$$$$$$$  |
 \_______|\__|\__| \_______|\__|  \__|  \____/ \_______/
**/

INSERT INTO
    "clients" (
        "full_name",
        "phone",
        "email",
        "created_at"
    )
VALUES (
        'Ronald A. Garcia',
        '+34 720 866 025',
        'RonaldAGarcia@armyspy.com',
        '2026-01-24'
    ),
    (
        'Patty J. Angel',
        '+34 721 502 667',
        'PattyJAngel@armyspy.com',
        '2026-01-24'
    ),
    (
        'Valerie K. Reeser',
        '+34 731 947 784',
        'ValerieKReeser@dayrep.com',
        '2026-01-23'
    ),
    (
        'Steve G. Whiteside',
        '+34 726 607 834',
        'SteveGWhiteside@armyspy.com',
        '2026-01-20'
    ),
    (
        'Natasha G. Smith',
        '+34 650 994 513',
        'NatashaGSmith@teleworm.us',
        '2025-12-01'
    );

/*
                                                             $$\               
                                                             $$ |              
 $$$$$$\   $$$$$$$\  $$$$$$$\  $$$$$$\  $$\   $$\ $$$$$$$\ $$$$$$\    $$$$$$$\ 
 \____$$\ $$  _____|$$  _____|$$  __$$\ $$ |  $$ |$$  __$$\\_$$  _|  $$  _____|
 $$$$$$$ |$$ /      $$ /      $$ /  $$ |$$ |  $$ |$$ |  $$ | $$ |    \$$$$$$\  
$$  __$$ |$$ |      $$ |      $$ |  $$ |$$ |  $$ |$$ |  $$ | $$ |$$\  \____$$\ 
\$$$$$$$ |\$$$$$$$\ \$$$$$$$\ \$$$$$$  |\$$$$$$  |$$ |  $$ | \$$$$  |$$$$$$$  |
 \_______| \_______| \_______| \______/  \______/ \__|  \__|  \____/ \_______/
**/

INSERT INTO
    "accounts" (
        "client_id",
        "account_type",
        "currency",
        "opened_at",
        "status"
    )
VALUES (
        1,
        'debit',
        'EUR',
        '2026-01-24',
        'active'
    ),
    (
        1,
        'savings',
        'USD',
        '2026-01-24',
        'active'
    ),
    (
        3,
        'credit',
        'GBP',
        '2026-01-23',
        'blocked'
    ),
    (
        4,
        'savings',
        'DKK',
        '2026-01-22',
        'closed'
    ),
    (
        2,
        'debit',
        'INR',
        '2026-01-24',
        'active'
    ),
    (
        4,
        'debit',
        'EUR',
        '2026-01-19',
        'active'
    ),
    (
        4,
        'credit',
        'CNY',
        '2025-12-30',
        'closed'
    );

/*
                                   $$\           
                                   $$ |          
 $$$$$$$\ $$$$$$\   $$$$$$\   $$$$$$$ | $$$$$$$\ 
$$  _____|\____$$\ $$  __$$\ $$  __$$ |$$  _____|
$$ /      $$$$$$$ |$$ |  \__|$$ /  $$ |\$$$$$$\  
$$ |     $$  __$$ |$$ |      $$ |  $$ | \____$$\ 
\$$$$$$$\\$$$$$$$ |$$ |      \$$$$$$$ |$$$$$$$  |
 \_______|\_______|\__|       \_______|\_______/
**/

INSERT INTO
    "cards" (
        "account_id",
        "card_type",
        "issued_at",
        "expires_at",
        "status"
    )
VALUES (
        1,
        'debit',
        '2026-01-24',
        '2027-01-24',
        'active'
    ),
    (
        2,
        'virtual',
        '2026-01-24',
        '2027-01-24',
        'active'
    ),
    (
        3,
        'credit',
        '2026-01-23',
        '2027-01-23',
        'blocked'
    ),
    (
        4,
        'virtual',
        '2025-01-22',
        '2026-01-22',
        'expired'
    ),
    (
        1,
        'credit',
        '2025-01-30',
        '2026-01-30',
        'active'
    );

/*
  $$\                                                                  $$\     $$\                               
  $$ |                                                                 $$ |    \__|                              
$$$$$$\    $$$$$$\  $$$$$$\  $$$$$$$\   $$$$$$$\  $$$$$$\   $$$$$$$\ $$$$$$\   $$\  $$$$$$\  $$$$$$$\   $$$$$$$\ 
\_$$  _|  $$  __$$\ \____$$\ $$  __$$\ $$  _____| \____$$\ $$  _____|\_$$  _|  $$ |$$  __$$\ $$  __$$\ $$  _____|
  $$ |    $$ |  \__|$$$$$$$ |$$ |  $$ |\$$$$$$\   $$$$$$$ |$$ /        $$ |    $$ |$$ /  $$ |$$ |  $$ |\$$$$$$\  
  $$ |$$\ $$ |     $$  __$$ |$$ |  $$ | \____$$\ $$  __$$ |$$ |        $$ |$$\ $$ |$$ |  $$ |$$ |  $$ | \____$$\ 
  \$$$$  |$$ |     \$$$$$$$ |$$ |  $$ |$$$$$$$  |\$$$$$$$ |\$$$$$$$\   \$$$$  |$$ |\$$$$$$  |$$ |  $$ |$$$$$$$  |
   \____/ \__|      \_______|\__|  \__|\_______/  \_______| \_______|   \____/ \__| \______/ \__|  \__|\_______/
**/

INSERT INTO
    "transactions" (
        "account_id",
        "amount",
        "txn_type",
        "txn_date",
        "description"
    )
VALUES (
        2,
        200,
        'transfer_in',
        '2026-01-20 02:19:07',
        'Gift'
    ),
    (
        4,
        455,
        'withdrawal',
        '2025-12-27 21:35:10',
        NULL
    ),
    (
        2,
        5,
        'fee',
        '2026-01-15 00:00:00',
        'International payment fee'
    ),
    (
        5,
        27.11,
        'transfer_out',
        '2026-01-25 16:09:07',
        'Donation to charity'
    ),
    (
        3,
        100.99,
        'deposit',
        '2026-01-23 00:00:00',
        NULL
    );

/*
$$\                                         
$$ |                                        
$$ | $$$$$$\   $$$$$$\  $$$$$$$\   $$$$$$$\ 
$$ |$$  __$$\  \____$$\ $$  __$$\ $$  _____|
$$ |$$ /  $$ | $$$$$$$ |$$ |  $$ |\$$$$$$\  
$$ |$$ |  $$ |$$  __$$ |$$ |  $$ | \____$$\ 
$$ |\$$$$$$  |\$$$$$$$ |$$ |  $$ |$$$$$$$  |
\__| \______/  \_______|\__|  \__|\_______/
**/

INSERT INTO
    loans (
        client_id,
        principal,
        interest_rate,
        start_date,
        end_date,
        status
    )
VALUES (
        1,
        15000.00,
        7.50,
        '2024-12-05',
        '2025-12-05',
        'active'
    ),
    (
        2,
        8200.00,
        5.20,
        '2024-12-18',
        '2025-06-18',
        'closed'
    ),
    (
        3,
        25000.00,
        9.00,
        '2025-01-03',
        '2026-01-03',
        'active'
    ),
    (
        4,
        12000.50,
        6.75,
        '2025-01-10',
        '2025-10-10',
        'overdue'
    ),
    (
        5,
        5000.00,
        4.10,
        '2024-12-28',
        '2025-03-28',
        'closed'
    ),
    (
        5,
        15000.00,
        5.40,
        '2024-12-28',
        '2025-03-28',
        'active'
    );

/*
                                                                 $$\               
                                                                 $$ |              
 $$$$$$\   $$$$$$\  $$\   $$\ $$$$$$\$$$$\   $$$$$$\  $$$$$$$\ $$$$$$\    $$$$$$$\ 
$$  __$$\  \____$$\ $$ |  $$ |$$  _$$  _$$\ $$  __$$\ $$  __$$\\_$$  _|  $$  _____|
$$ /  $$ | $$$$$$$ |$$ |  $$ |$$ / $$ / $$ |$$$$$$$$ |$$ |  $$ | $$ |    \$$$$$$\  
$$ |  $$ |$$  __$$ |$$ |  $$ |$$ | $$ | $$ |$$   ____|$$ |  $$ | $$ |$$\  \____$$\ 
$$$$$$$  |\$$$$$$$ |\$$$$$$$ |$$ | $$ | $$ |\$$$$$$$\ $$ |  $$ | \$$$$  |$$$$$$$  |
$$  ____/  \_______| \____$$ |\__| \__| \__| \_______|\__|  \__|  \____/ \_______/ 
$$ |                $$\   $$ |                                                     
$$ |                \$$$$$$  |                                                     
\__|                 \______/
**/

INSERT INTO
    loan_payments (
        loan_id,
        amount,
        payment_date,
        status
    )
VALUES (
        1,
        1200.00,
        '2025-01-05',
        'success'
    ),
    (
        1,
        1200.00,
        '2025-02-05',
        'success'
    ),
    (
        1,
        1200.00,
        '2025-03-05',
        'success'
    ),
    (
        1,
        1200.00,
        '2025-04-05',
        'success'
    ),
    (
        2,
        1366.67,
        '2024-12-20',
        'success'
    ),
    (
        2,
        1366.67,
        '2025-01-20',
        'success'
    ),
    (
        2,
        1366.67,
        '2025-02-20',
        'success'
    ),
    (
        2,
        1366.67,
        '2025-03-20',
        'success'
    ),
    (
        2,
        1366.67,
        '2025-04-20',
        'success'
    ),
    (
        2,
        1366.67,
        '2025-05-20',
        'success'
    ),
    (
        3,
        2100.00,
        '2025-02-03',
        'success'
    ),
    (
        3,
        2100.00,
        '2025-03-03',
        'success'
    ),
    (
        3,
        2100.00,
        '2025-04-03',
        'success'
    ),
    (
        4,
        1500.00,
        '2025-02-10',
        'success'
    ),
    (
        4,
        1500.00,
        '2025-03-10',
        'failed'
    ),
    (
        4,
        1500.00,
        '2025-04-10',
        'failed'
    ),
    (
        5,
        1700.00,
        '2025-01-28',
        'success'
    ),
    (
        5,
        1700.00,
        '2025-02-28',
        'success'
    ),
    (
        5,
        1700.00,
        '2025-03-28',
        'success'
    ),
    (
        5,
        1700.00,
        '2025-04-28',
        'success'
    );

/*                                                           
 @@@@@@    @@@  @@@  @@@@@@@@  @@@@@@@   @@@  @@@@@@@@   @@@@@@   
@@@@@@@@   @@@  @@@  @@@@@@@@  @@@@@@@@  @@@  @@@@@@@@  @@@@@@@   
@@!  @@@   @@!  @@@  @@!       @@!  @@@  @@!  @@!       !@@       
!@!  @!@   !@!  @!@  !@!       !@!  @!@  !@!  !@!       !@!       
@!@  !@!   @!@  !@!  @!!!:!    @!@!!@!   !!@  @!!!:!    !!@@!!    
!@!  !!!   !@!  !!!  !!!!!:    !!@!@!    !!!  !!!!!:     !!@!!!   
!!:!!:!:   !!:  !!!  !!:       !!: :!!   !!:  !!:            !:!  
:!: :!:    :!:  !:!  :!:       :!:  !:!  :!:  :!:           !:!   
::::: :!   ::::: ::   :: ::::  ::   :::   ::   :: ::::  :::: ::   
 : :  :::   : :  :   : :: ::    :   : :  :    : :: ::   :: : :
**/

-- ========================================================================================
-- 1) Найти активные счета в EUR, открытые после 2024-01-01, отсортировать по дате открытия
-- ========================================================================================

SELECT *
FROM accounts
WHERE
    opened_at > '2024-01-01'
    AND currency = 'EUR'
    AND status = 'active'
ORDER BY opened_at;

-- ========================================================
-- 2) Вывести ФИО клиента, тип счета, валюту и статус счета
-- ========================================================

SELECT clients.full_name, accounts.account_type, accounts.currency, accounts.status
FROM clients
    JOIN accounts ON clients.client_id = accounts.client_id;

/*
⚠️ Если необходимо также вывести ФИО клиентов, у которых нет счета, то используйте LEFT JOIN:

SELECT clients.full_name, accounts.account_type, accounts.currency, accounts.status
FROM clients
    LEFT JOIN accounts ON clients.client_id = accounts.client_id;
**/

-- ===========================================================
-- 3) Вывести всех клиентов и количество их счетов (включая 0)
-- ===========================================================

SELECT clients.*, COUNT(accounts.account_id) AS account_count
FROM clients
    LEFT JOIN accounts ON clients.client_id = accounts.client_id
GROUP BY
    clients.client_id
ORDER BY clients.client_id;

/*
⚠️ Использовано SELECT clients.* для более короткой записи, но best practice – делать явное перечисление столбцов ради лучшего контроля вывода
**/

-- =====================================================
-- 4) Найти клиентов, у которых больше 2 активных счетов
-- =====================================================

SELECT clients.*, COUNT(accounts.account_id) AS account_count
FROM clients
    JOIN accounts ON clients.client_id = accounts.client_id
WHERE
    accounts.status = 'active'
GROUP BY
    clients.client_id
HAVING
    COUNT(accounts.account_id) > 2
ORDER BY account_count DESC;

-- ================================================================================================
-- 5) Найти счета, у которых сумма входящих операций (deposit + transfer_in) выше среднего по банку
-- ================================================================================================

SELECT accounts.*, sum(transactions.amount)
FROM accounts
    JOIN transactions ON accounts.account_id = transactions.account_id
WHERE
    transactions.txn_type IN ('deposit', 'transfer_in')
GROUP BY
    accounts.account_id
HAVING
    sum(transactions.amount) > (
        SELECT avg(amount)
        FROM transactions
    );

-- =============================================================
-- 6) Топ-5 клиентов по сумме всех операций (оборот) за 2025 год
-- =============================================================

SELECT clients.*, sum(transactions.amount) AS transactions_sum
FROM
    clients
    JOIN accounts ON clients.client_id = accounts.client_id
    JOIN transactions ON accounts.account_id = transactions.account_id
WHERE
    transactions.txn_date BETWEEN '2025-01-01 00:00:00' AND '2025-12-31 23:59:59'
GROUP BY
    clients.client_id
ORDER BY transactions_sum DESC
LIMIT 5;

-- ===============================================================================
-- 7) Определить “активность клиента” по количеству операций за последние 90 дней:
--      0 операций → inactive
--      1–5 → low
--      6–20 → medium
--      20 → high
-- ===============================================================================

WITH
    transactions_per_user AS (
        SELECT clients.*, count(transactions.transaction_id) transactions_count
        FROM
            clients
            LEFT JOIN accounts ON clients.client_id = accounts.client_id
            LEFT JOIN transactions ON transactions.account_id = accounts.account_id
            AND transactions.txn_date > now() - interval '90 days'
        GROUP BY
            clients.client_id
    )
SELECT
    *,
    CASE
        WHEN transactions_per_user.transactions_count = 0 THEN 'inactive'
        WHEN transactions_per_user.transactions_count BETWEEN 1 AND 5  THEN 'low'
        WHEN transactions_per_user.transactions_count BETWEEN 6 AND 20  THEN 'medium'
        ELSE 'high'
    END activity
FROM transactions_per_user
ORDER BY transactions_count DESC;

-- Или без CTE:

SELECT
    clients.*,
    CASE
        WHEN count(transactions.transaction_id) = 0 THEN 'inactive'
        WHEN count(transactions.transaction_id) BETWEEN 1 AND 5  THEN 'low'
        WHEN count(transactions.transaction_id) BETWEEN 6 AND 20  THEN 'medium'
        ELSE 'high'
    END activity
FROM
    clients
    LEFT JOIN accounts ON clients.client_id = accounts.client_id
    LEFT JOIN transactions ON transactions.account_id = accounts.account_id
    AND transactions.txn_date > now() - interval '90 days'
GROUP BY
    clients.client_id
ORDER BY count(transactions.transaction_id) DESC;

-- =======================================================================
-- 8) Найти кредиты, по которым сумма успешных платежей < 50% от principal
-- =======================================================================

SELECT loans.*, COALESCE(sum(loan_payments.amount), 0) total_payments
FROM loans
    LEFT JOIN loan_payments ON loans.loan_id = loan_payments.loan_id
    AND loan_payments.status = 'success'
GROUP BY
    loans.loan_id
HAVING
    COALESCE(sum(loan_payments.amount), 0) < loans.principal * 0.5;

-- ======================================================================================
-- 9) Показать все активные карты и кому они принадлежат (ФИО, account_id, срок действия)
-- ======================================================================================

SELECT clients.full_name, accounts.account_id, cards.issued_at, cards.expires_at
FROM cards
    JOIN accounts ON cards.account_id = accounts.account_id
    JOIN clients ON accounts.client_id = clients.client_id
WHERE
    cards.status = 'active';

-- =======================================================================================================
-- 10) Для каждого счета посчитать: количество операций и сумму списаний (withdrawal + transfer_out + fee)
-- =======================================================================================================

SELECT
    accounts.*,
    count(transactions.transaction_id) transactions_amount,
    sum(
        CASE
            WHEN transactions.txn_type IN (
                'withdrawal',
                'transfer_out',
                'fee'
            ) THEN transactions.amount
            ELSE 0
        END
    ) withdrawals_sum
FROM accounts
    LEFT JOIN transactions ON accounts.account_id = transactions.account_id
GROUP BY
    accounts.account_id;