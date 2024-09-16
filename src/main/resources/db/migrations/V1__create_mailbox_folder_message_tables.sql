CREATE TABLE IF NOT EXISTS mailbox (
                                       idt SERIAL PRIMARY KEY,
                                       name TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS folder (
                                      idt SERIAL PRIMARY KEY,
                                      name TEXT NOT NULL,
                                      mailbox_idt INT NOT NULL,
                                      UNIQUE(name, mailbox_idt),
    FOREIGN KEY (mailbox_idt) REFERENCES mailbox(idt) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS message (
                                       idt SERIAL PRIMARY KEY,
                                       sender TEXT NOT NULL,
                                       recipient TEXT NOT NULL,
                                       subject TEXT,
                                       body TEXT,
                                       read BOOLEAN NOT NULL,
                                       send_at TIMESTAMP NOT NULL,
                                       folder_idt INT NOT NULL,
                                       FOREIGN KEY (folder_idt) REFERENCES folder(idt) ON DELETE CASCADE
    );