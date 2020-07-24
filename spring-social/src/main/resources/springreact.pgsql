--
-- PostgreSQL database dump
--

-- Dumped from database version 12.3 (Ubuntu 12.3-1.pgdg20.04+1)
-- Dumped by pg_dump version 12.3 (Ubuntu 12.3-1.pgdg20.04+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categories (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(500),
    parent_id integer
);


ALTER TABLE public.categories OWNER TO postgres;

--
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categories_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.categories_id_seq OWNER TO postgres;

--
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categories_id_seq OWNED BY public.categories.id;


--
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    id integer NOT NULL,
    post_id integer NOT NULL,
    user_created_id integer NOT NULL,
    date_created timestamp without time zone DEFAULT now() NOT NULL,
    message character varying(1000) NOT NULL,
    parent_id integer,
    date_updated timestamp without time zone,
    user_updated_id integer,
    is_deleted boolean DEFAULT false
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comments_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.comments_id_seq OWNER TO postgres;

--
-- Name: comments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.comments_id_seq OWNED BY public.comments.id;


--
-- Name: files; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.files (
    id integer NOT NULL,
    title character varying(255),
    file_name character varying(255) NOT NULL,
    file_path character varying(255) NOT NULL,
    file_type character varying(128) NOT NULL,
    date_uploaded timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.files OWNER TO postgres;

--
-- Name: files_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.files_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.files_id_seq OWNER TO postgres;

--
-- Name: files_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.files_id_seq OWNED BY public.files.id;


--
-- Name: images; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.images (
    id integer NOT NULL,
    title character varying(255),
    file_name character varying(255) NOT NULL,
    file_path character varying(255) NOT NULL,
    height integer NOT NULL,
    width integer NOT NULL,
    date_uploaded timestamp without time zone DEFAULT now() NOT NULL,
    file_type character varying(128) NOT NULL
);


ALTER TABLE public.images OWNER TO postgres;

--
-- Name: images_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.images_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.images_id_seq OWNER TO postgres;

--
-- Name: images_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.images_id_seq OWNED BY public.images.id;


--
-- Name: post_files; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.post_files (
    post_id integer NOT NULL,
    file_id integer NOT NULL
);


ALTER TABLE public.post_files OWNER TO postgres;

--
-- Name: post_images; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.post_images (
    post_id integer NOT NULL,
    image_id integer NOT NULL
);


ALTER TABLE public.post_images OWNER TO postgres;

--
-- Name: post_tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.post_tags (
    id_tag integer NOT NULL,
    id_post integer NOT NULL
);


ALTER TABLE public.post_tags OWNER TO postgres;

--
-- Name: posts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.posts (
    id integer NOT NULL,
    title character varying(255) NOT NULL,
    date_created timestamp without time zone DEFAULT now() NOT NULL,
    date_posted timestamp without time zone,
    date_updated timestamp without time zone,
    description character varying(500),
    post_text text NOT NULL,
    user_created_id integer NOT NULL,
    user_updated_id integer,
    posted boolean DEFAULT false,
    category_id integer NOT NULL,
    preview_image_id integer
);


ALTER TABLE public.posts OWNER TO postgres;

--
-- Name: posts_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.posts_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.posts_id_seq OWNER TO postgres;

--
-- Name: posts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.posts_id_seq OWNED BY public.posts.id;


--
-- Name: tags; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tags (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(500)
);


ALTER TABLE public.tags OWNER TO postgres;

--
-- Name: tags_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tags_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tags_id_seq OWNER TO postgres;

--
-- Name: tags_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tags_id_seq OWNED BY public.tags.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    email_verified boolean DEFAULT false NOT NULL,
    image_url character varying(255),
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    provider character varying(255) NOT NULL,
    role_name character varying(128) DEFAULT 'ROLE_USER'::character varying NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1
);


--
-- Name: categories id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories ALTER COLUMN id SET DEFAULT nextval('public.categories_id_seq'::regclass);


--
-- Name: comments id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments ALTER COLUMN id SET DEFAULT nextval('public.comments_id_seq'::regclass);


--
-- Name: files id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.files ALTER COLUMN id SET DEFAULT nextval('public.files_id_seq'::regclass);


--
-- Name: images id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.images ALTER COLUMN id SET DEFAULT nextval('public.images_id_seq'::regclass);


--
-- Name: posts id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts ALTER COLUMN id SET DEFAULT nextval('public.posts_id_seq'::regclass);


--
-- Name: tags id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tags ALTER COLUMN id SET DEFAULT nextval('public.tags_id_seq'::regclass);


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categories (id, name, description, parent_id) FROM stdin;
1	Java	Java	\N
2	Python	Python	\N
3	Spring Boot	Spring Boot	1
4	Hibernate	Hibernate	1
5	Spring Security	Spring Security	3
6	Test	Test	\N
7	Check	Check	\N
\.


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comments (id, post_id, user_created_id, date_created, message, parent_id, date_updated, user_updated_id, is_deleted) FROM stdin;
51	24	5	2020-07-08 17:37:50.979	test	\N	2020-07-08 17:37:50.979	5	f
10	25	5	2020-07-08 15:28:49.612	qewqwe	\N	2020-07-08 15:28:49.612	5	f
11	25	5	2020-07-08 15:28:51.832	qewqwe	\N	2020-07-08 15:28:51.832	5	f
12	25	5	2020-07-08 15:28:53.94	qwe	\N	2020-07-08 15:28:53.94	5	f
13	25	5	2020-07-08 15:29:22.019	qwe	\N	2020-07-08 15:29:22.019	5	f
14	25	5	2020-07-08 15:29:23.65	qwe	\N	2020-07-08 15:29:23.65	5	f
15	25	5	2020-07-08 15:29:25.457	qwe	\N	2020-07-08 15:29:25.457	5	f
16	25	5	2020-07-08 15:29:34.291	qwe	\N	2020-07-08 15:29:34.291	5	f
17	26	5	2020-07-08 15:30:19.762	qwe	\N	2020-07-08 15:30:19.762	5	f
18	26	5	2020-07-08 15:30:21.439	qwe	\N	2020-07-08 15:30:21.439	5	f
19	26	5	2020-07-08 15:30:23.392	12331	\N	2020-07-08 15:30:23.392	5	f
20	26	5	2020-07-08 15:32:08.975	йцуй	\N	2020-07-08 15:32:08.975	5	f
8	2	5	2020-07-08 15:26:36.374	Сообщено удалено	\N	2020-07-08 16:01:25.036	5	t
1	2	5	2020-07-04 19:18:05	Сообщено удалено	\N	2020-07-08 16:01:44.668	5	t
7	2	5	2020-07-08 15:26:28.569	Сообщено удалено	\N	2020-07-08 16:04:55.764	5	t
3	2	5	2020-07-08 15:12:48.486	Сообщено удалено	\N	2020-07-08 16:09:01.642	5	t
4	2	5	2020-07-08 15:20:03.526	Сообщено удалено	\N	2020-07-08 16:14:29.911	5	t
2	2	5	2020-07-04 19:31:36	Сообщено удалено	1	2020-07-08 16:36:55.413	5	t
26	2	5	2020-07-08 16:37:38.497	Сообщено удалено	24	2020-07-08 16:38:26.396	5	t
24	2	5	2020-07-08 16:36:23.623	Сообщено удалено	\N	2020-07-08 16:46:05.038	5	t
27	2	5	2020-07-08 16:38:31.763	Сообщено удалено	24	2020-07-08 16:46:32.4	5	t
37	2	5	2020-07-08 16:46:28.021	Сообщено удалено	27	2020-07-08 16:46:33.959	5	t
39	2	5	2020-07-08 16:46:31.704	Сообщено удалено	27	2020-07-08 16:46:34.859	5	t
38	2	5	2020-07-08 16:46:29.982	Сообщено удалено	37	2020-07-08 16:46:35.682	5	t
23	2	5	2020-07-08 16:34:08.55	Сообщено удалено	\N	2020-07-08 16:46:37.431	5	t
22	2	5	2020-07-08 16:34:02.625	Сообщено удалено	\N	2020-07-08 16:46:38.022	5	t
21	2	5	2020-07-08 16:33:45.96	Сообщено удалено	\N	2020-07-08 16:46:39.043	5	t
6	2	5	2020-07-08 15:24:20.09	Сообщено удалено	\N	2020-07-08 16:46:40.134	5	t
28	2	5	2020-07-08 16:44:39.56	Сообщено удалено	6	2020-07-08 16:46:40.678	5	t
29	2	5	2020-07-08 16:44:43.457	Сообщено удалено	28	2020-07-08 16:46:41.652	5	t
33	2	5	2020-07-08 16:44:54.291	Сообщено удалено	32	2020-07-08 16:46:42.425	5	t
31	2	5	2020-07-08 16:44:47.554	Сообщено удалено	30	2020-07-08 16:46:44.334	5	t
5	2	5	2020-07-08 15:23:07.295	Сообщено удалено	\N	2020-07-08 16:46:45.599	5	t
36	2	5	2020-07-08 16:46:23.998	Сообщено удалено	35	2020-07-08 16:46:50.459	5	t
35	2	5	2020-07-08 16:46:12.503	Сообщено удалено	34	2020-07-08 16:46:51.075	5	t
34	2	5	2020-07-08 16:46:10.062	Сообщено удалено	25	2020-07-08 16:46:52.391	5	t
25	2	5	2020-07-08 16:37:30.28	Сообщено удалено	24	2020-07-08 16:46:53.422	5	t
32	2	5	2020-07-08 16:44:50.351	Сообщено удалено	28	2020-07-08 16:46:58.761	5	t
30	2	5	2020-07-08 16:44:45.537	Сообщено удалено	29	2020-07-08 16:46:59.641	5	t
40	26	5	2020-07-08 16:48:47.256	qweq	20	2020-07-08 16:48:47.256	5	f
41	26	5	2020-07-08 16:48:49.969	Сообщено удалено	40	2020-07-08 16:48:51.196	5	t
42	26	5	2020-07-08 16:48:53.842	123123	19	2020-07-08 16:48:53.842	5	f
43	26	5	2020-07-08 16:48:56.058	weqwe	18	2020-07-08 16:48:56.058	5	f
44	26	5	2020-07-08 16:48:57.558	eqweqe	\N	2020-07-08 16:48:57.558	5	f
45	26	5	2020-07-08 16:49:01.262	qweqwe	\N	2020-07-08 16:49:01.262	5	f
46	26	5	2020-07-08 16:49:04.597	13213	\N	2020-07-08 16:49:04.597	5	f
47	26	5	2020-07-08 16:51:55.846	123123	\N	2020-07-08 16:51:55.846	5	f
48	26	5	2020-07-08 16:52:04.668	13qewqeweqe	\N	2020-07-08 16:52:04.668	5	f
50	8	5	2020-07-08 17:09:53.091	Сообщено удалено	49	2020-07-08 17:09:56.515	5	t
49	8	5	2020-07-08 17:09:49.757	Сообщено удалено	\N	2020-07-08 17:09:57.662	5	t
52	24	5	2020-07-08 17:38:56.381	qweqweqwe1231323123123123132	51	2020-07-08 17:38:56.381	5	f
53	24	5	2020-07-08 17:38:58.932	123123	\N	2020-07-08 17:38:58.932	5	f
54	24	5	2020-07-08 17:39:02.564	123123	\N	2020-07-08 17:39:02.564	5	f
55	24	5	2020-07-08 17:39:27.969	qweqe	51	2020-07-08 17:39:27.969	5	f
56	24	5	2020-07-08 17:39:30.674	qweqq	51	2020-07-08 17:39:30.674	5	f
57	27	5	2020-07-10 17:13:50.13	Сообщение удалено	\N	2020-07-10 17:13:51.385	5	t
58	27	5	2020-07-10 17:13:53.138	123123	\N	2020-07-10 17:13:53.138	5	f
59	27	5	2020-07-10 17:13:55.833	123123	\N	2020-07-10 17:13:55.833	5	f
60	27	5	2020-07-10 17:14:05.842	qweqweqwe	58	2020-07-10 17:14:05.842	5	f
61	27	5	2020-07-10 17:14:08.36	test	60	2020-07-10 17:14:08.36	5	f
62	13	5	2020-07-12 20:54:40.186	test	\N	2020-07-12 20:54:40.186	5	f
63	13	5	2020-07-12 20:54:42.492	test	\N	2020-07-12 20:54:42.492	5	f
64	13	5	2020-07-12 20:54:48.532	test	\N	2020-07-12 20:54:48.532	5	f
65	13	5	2020-07-12 20:54:54.206	test	\N	2020-07-12 20:54:54.206	5	f
66	13	5	2020-07-12 20:58:13.71	qwe	64	2020-07-12 20:58:13.71	5	f
\.


--
-- Data for Name: files; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.files (id, title, file_name, file_path, file_type, date_uploaded) FROM stdin;
\.


--
-- Data for Name: images; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.images (id, title, file_name, file_path, height, width, date_uploaded, file_type) FROM stdin;
3	\N	1_mk1-6aYaf_Bes1E3Imhc0A.jpeg	/home/leonid/Рабочий стол/spring-boot-react/images/	768	1366	2020-07-05 17:05:53.556	image/jpeg
\.


--
-- Data for Name: post_files; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.post_files (post_id, file_id) FROM stdin;
\.


--
-- Data for Name: post_images; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.post_images (post_id, image_id) FROM stdin;
2	3
5	3
6	3
7	3
8	3
9	3
10	3
11	3
12	3
13	3
14	3
15	3
16	3
17	3
18	3
19	3
20	3
21	3
22	3
23	3
24	3
25	3
26	3
27	3
\.


--
-- Data for Name: post_tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.post_tags (id_tag, id_post) FROM stdin;
2	2
1	2
1	3
1	5
2	5
3	5
4	5
1	6
2	6
3	6
4	6
1	7
2	7
3	7
4	7
1	8
2	8
3	8
4	8
1	9
2	9
3	9
4	9
1	10
2	10
3	10
4	10
1	11
2	11
3	11
4	11
1	12
2	12
3	12
4	12
1	13
2	13
3	13
4	13
1	14
2	14
3	14
4	14
1	15
2	15
3	15
4	15
1	16
2	16
3	16
4	16
1	17
2	17
3	17
4	17
1	18
2	18
3	18
4	18
1	19
2	19
3	19
4	19
1	20
2	20
3	20
4	20
1	21
2	21
3	21
4	21
1	22
2	22
3	22
4	22
1	23
2	23
3	23
4	23
1	24
2	24
3	24
4	24
1	25
2	25
3	25
4	25
1	26
2	26
3	26
4	26
1	27
2	27
3	27
4	27
\.


--
-- Data for Name: posts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.posts (id, title, date_created, date_posted, date_updated, description, post_text, user_created_id, user_updated_id, posted, category_id, preview_image_id) FROM stdin;
26	ipfkdqakgqrgdzifdheyiel	2020-07-06 12:36:20.792	\N	2020-07-06 12:36:20.792	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	f	2	\N
27	vyvqficvakanpbnmxozdans	2020-07-06 12:36:20.804	2020-07-06 12:36:20.8	2020-07-06 12:36:20.804	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	1	3
24	dpkmbtsqjbkcqmrmdzsthmr	2020-07-06 12:36:20.762	2020-07-06 12:36:20.758	2020-07-06 12:36:20.762	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	4	3
25	bcqqftagrsfivpsesviuuvc	2020-07-06 12:36:20.776	\N	2020-07-06 12:36:20.776	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	f	1	3
22	rkvzfmbvpvfivjdclpfcpph	2020-07-06 12:36:20.735	\N	2020-07-06 12:36:20.735	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	f	4	3
23	vceofzyhkwuqxmgoqqymogf	2020-07-06 12:36:20.749	\N	2020-07-06 12:36:20.749	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	f	2	\N
20	daboglwbptivfgkwoksuazd	2020-07-06 12:36:20.706	2020-07-06 12:36:20.7	2020-07-06 12:36:20.706	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	3	\N
21	axqclvrxfxhhalpdwmdcoye	2020-07-06 12:36:20.722	\N	2020-07-06 12:36:20.722	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	f	4	3
18	rhgnffgqgypgaijwmuxdcfk	2020-07-06 12:36:20.673	2020-07-06 12:36:20.668	2020-07-06 12:36:20.673	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	1	3
19	cdyqlhladqbfzmhuxhojhyt	2020-07-06 12:36:20.689	2020-07-06 12:36:20.684	2020-07-06 12:36:20.689	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	1	\N
16	nlzgiijprtbtpftfrudsons	2020-07-06 12:36:20.642	\N	2020-07-06 12:36:20.642	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	f	2	3
17	zbezvnmtxgemozitpxuwfnq	2020-07-06 12:36:20.658	\N	2020-07-06 12:36:20.658	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	f	4	3
14	chpbbddqqfhdkricxmbevah	2020-07-06 12:36:20.611	2020-07-06 12:36:20.606	2020-07-06 12:36:20.611	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	4	3
15	qiwdldftllzdoxfkchgtsei	2020-07-06 12:36:20.627	2020-07-06 12:36:20.622	2020-07-06 12:36:20.627	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	1	3
12	ltfubxnybpoghcahnusvnhm	2020-07-06 12:36:20.579	2020-07-06 12:36:20.573	2020-07-06 12:36:20.579	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	4	\N
13	xsofrbwalyhfycwdahbartz	2020-07-06 12:36:20.595	2020-07-06 12:36:20.59	2020-07-06 12:36:20.595	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	4	3
10	noopfoebveijppnetwkajyy	2020-07-06 12:36:20.547	\N	2020-07-06 12:36:20.547	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	f	2	\N
11	mvzapfnqiebljeugdzxvrri	2020-07-06 12:36:20.563	2020-07-06 12:36:20.558	2020-07-06 12:36:20.563	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	2	3
8	phndbtujkjamgcgepnyhvmy	2020-07-06 12:36:20.511	2020-07-06 12:36:20.505	2020-07-06 12:36:20.511	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	2	\N
9	umsttdrpzlqgaxamziabuqx	2020-07-06 12:36:20.527	\N	2020-07-06 12:36:20.527	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	f	1	\N
6	mrvgpgvfxapqjayhbyweqft	2020-07-06 12:36:20.473	2020-07-06 12:36:20.466	2020-07-06 12:36:20.473	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	3	\N
7	caktombddbwbueefmjzmhfp	2020-07-06 12:36:20.493	\N	2020-07-06 12:36:20.493	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	f	3	\N
5	zycpwapmczohrnrciwbejxb	2020-07-06 12:36:20.413	2020-07-06 12:36:20.344	2020-07-06 12:36:20.413	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	2	\N
2	oqiheoqhwe	2020-07-04 19:15:57	2020-07-04 19:16:00	\N	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	\N	t	1	3
3	qweqwe	2020-07-04 21:42:57	2020-07-04 21:43:00	2020-07-04 21:43:01	The quick brown fox jumps over the lazy dog	The quick brown fox jumps over the lazy dog	5	5	t	3	\N
\.


--
-- Data for Name: tags; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tags (id, name, description) FROM stdin;
1	qewqew	qweqwe
2	qweqweqqwesad	qeqweqwe
3	uqweq	qeqwe
4	12u3noqw	qweqe
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, email_verified, image_url, name, password, provider, role_name) FROM stdin;
6	euqhweo@qweoqhwe	f	\N	qweqeweqw	$2a$10$20SkLZACr5.fjc3yss560OlciWsj1XydF70p7HawQjZWieKlmR/oa	LOCAL	ROLE_USER
5	malkiev96@gmail.com	f	https://lh3.googleusercontent.com/-2m3O9IA4NXE/AAAAAAAAAAI/AAAAAAAABSo/AMZuucldbQ6ZaGv5yruORRbiuJa8n665cQ/photo.jpg	Леонид	$2a$10$Mf/bjjy3aIGH06jKbDcT0OY357tMfhotlhe3Q4Yhzmau0ot4xRlHe	GOOGLE	ROLE_ADMIN
\.


--
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categories_id_seq', 7, true);


--
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comments_id_seq', 66, true);


--
-- Name: files_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.files_id_seq', 1, false);


--
-- Name: images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.images_id_seq', 3, true);


--
-- Name: posts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.posts_id_seq', 27, true);


--
-- Name: tags_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tags_id_seq', 4, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 6, true);


--
-- Name: categories categories_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pk PRIMARY KEY (id);


--
-- Name: comments comments_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pk PRIMARY KEY (id);


--
-- Name: files files_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.files
    ADD CONSTRAINT files_pk PRIMARY KEY (id);


--
-- Name: images images_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.images
    ADD CONSTRAINT images_pk PRIMARY KEY (id);


--
-- Name: posts posts_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_pk PRIMARY KEY (id);


--
-- Name: tags tags_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_pk PRIMARY KEY (id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: categories_name_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX categories_name_uindex ON public.categories USING btree (name);


--
-- Name: files_file_name_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX files_file_name_uindex ON public.files USING btree (file_name);


--
-- Name: images_filename_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX images_filename_uindex ON public.images USING btree (file_name);


--
-- Name: post_files_post_id_file_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX post_files_post_id_file_id_uindex ON public.post_files USING btree (post_id DESC, file_id DESC);


--
-- Name: post_images_post_id_image_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX post_images_post_id_image_id_uindex ON public.post_images USING btree (post_id, image_id);


--
-- Name: post_tag_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX post_tag_uindex ON public.post_tags USING btree (id_tag, id_post);


--
-- Name: tags_name_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX tags_name_uindex ON public.tags USING btree (name);


--
-- Name: categories categories_categories_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_categories_id_fk FOREIGN KEY (parent_id) REFERENCES public.categories(id);


--
-- Name: comments comments_comments_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_comments_id_fk FOREIGN KEY (parent_id) REFERENCES public.comments(id);


--
-- Name: comments comments_posts_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_posts_id_fk FOREIGN KEY (post_id) REFERENCES public.posts(id);


--
-- Name: comments comments_users_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_users_id_fk FOREIGN KEY (user_created_id) REFERENCES public.users(id);


--
-- Name: comments comments_users_id_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_users_id_fk_2 FOREIGN KEY (user_updated_id) REFERENCES public.users(id);


--
-- Name: post_files post_files_files_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_files
    ADD CONSTRAINT post_files_files_id_fk FOREIGN KEY (file_id) REFERENCES public.files(id);


--
-- Name: post_files post_files_posts_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_files
    ADD CONSTRAINT post_files_posts_id_fk FOREIGN KEY (post_id) REFERENCES public.posts(id);


--
-- Name: post_images post_images_images_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_images
    ADD CONSTRAINT post_images_images_id_fk FOREIGN KEY (image_id) REFERENCES public.images(id);


--
-- Name: post_images post_images_posts_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_images
    ADD CONSTRAINT post_images_posts_id_fk FOREIGN KEY (post_id) REFERENCES public.posts(id);


--
-- Name: post_tags post_tags_posts_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_tags
    ADD CONSTRAINT post_tags_posts_id_fk FOREIGN KEY (id_post) REFERENCES public.posts(id);


--
-- Name: post_tags post_tags_tags_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.post_tags
    ADD CONSTRAINT post_tags_tags_id_fk FOREIGN KEY (id_tag) REFERENCES public.tags(id);


--
-- Name: posts posts_categories_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_categories_id_fk FOREIGN KEY (category_id) REFERENCES public.categories(id);


--
-- Name: posts posts_images_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_images_id_fk FOREIGN KEY (preview_image_id) REFERENCES public.images(id);


--
-- Name: posts posts_users_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_users_id_fk FOREIGN KEY (user_created_id) REFERENCES public.users(id);


--
-- Name: posts posts_users_id_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_users_id_fk_2 FOREIGN KEY (user_updated_id) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--

