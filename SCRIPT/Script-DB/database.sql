-- Table: public.slackchannel

-- DROP TABLE IF EXISTS public.slackchannel;

CREATE TABLE IF NOT EXISTS public.slackchannel
(
    id uuid NOT NULL,
    webhook character varying COLLATE pg_catalog."default" NOT NULL,
    channelname character varying COLLATE pg_catalog."default" NOT NULL,
    status character varying COLLATE pg_catalog."default",
    created_at timestamp with time zone,
    modified_at timestamp with time zone,
    CONSTRAINT slackchannel_pkey PRIMARY KEY (id),
    CONSTRAINT slackchannel_id_key UNIQUE (id),
    CONSTRAINT slackchannel_webhook_key UNIQUE (webhook)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.slackchannel
    OWNER to postgres;
	
CREATE TABLE IF NOT EXISTS public.logmessages
(
    id uuid NOT NULL,
    message character varying COLLATE pg_catalog."default" NOT NULL,
    "time" timestamp with time zone NOT NULL,
    logmessages_slackchannel uuid,
    CONSTRAINT logmessages_pkey PRIMARY KEY (id, message, "time"),
    CONSTRAINT logmessages_slackchannel FOREIGN KEY (logmessages_slackchannel)
        REFERENCES public.slackchannel (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.logmessages
    OWNER to postgres;