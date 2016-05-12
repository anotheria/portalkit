ALTER TABLE public.match DROP CONSTRAINT match_pkey;
ALTER TABLE public.match ADD CONSTRAINT match_pkey PRIMARY KEY (owner, target, type);
