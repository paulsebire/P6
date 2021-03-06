PGDMP                          x         
   amisgrimpe    12.1    12.1 .    N           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            O           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            P           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            Q           1262    16393 
   amisgrimpe    DATABASE     �   CREATE DATABASE amisgrimpe WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';
    DROP DATABASE amisgrimpe;
                postgres    false            �            1259    90638    commentaire    TABLE     �   CREATE TABLE public.commentaire (
    id bigint NOT NULL,
    contenu text,
    date timestamp without time zone,
    id_site bigint,
    id_user bigint
);
    DROP TABLE public.commentaire;
       public         heap    postgres    false            �            1259    90646    cotation    TABLE     _   CREATE TABLE public.cotation (
    id_cote bigint NOT NULL,
    cote character varying(255)
);
    DROP TABLE public.cotation;
       public         heap    postgres    false            �            1259    90636    hibernate_sequence    SEQUENCE     {   CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.hibernate_sequence;
       public          postgres    false            �            1259    90653    longueur    TABLE     �   CREATE TABLE public.longueur (
    id_longueur bigint NOT NULL,
    hauteur_longueur double precision,
    nom_longueur character varying(255),
    id_voie bigint
);
    DROP TABLE public.longueur;
       public         heap    postgres    false            �            1259    90651    longueur_id_longueur_seq    SEQUENCE     �   CREATE SEQUENCE public.longueur_id_longueur_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.longueur_id_longueur_seq;
       public          postgres    false    206            R           0    0    longueur_id_longueur_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.longueur_id_longueur_seq OWNED BY public.longueur.id_longueur;
          public          postgres    false    205            �            1259    90659    reservation    TABLE     �   CREATE TABLE public.reservation (
    id bigint NOT NULL,
    acceptation boolean,
    cloturer boolean,
    date timestamp without time zone,
    demande_en_cours boolean,
    id_topo bigint,
    id_user bigint
);
    DROP TABLE public.reservation;
       public         heap    postgres    false            �            1259    90664    roles    TABLE     ]   CREATE TABLE public.roles (
    id_user bigint NOT NULL,
    roles character varying(255)
);
    DROP TABLE public.roles;
       public         heap    postgres    false            �            1259    90669    site    TABLE     �   CREATE TABLE public.site (
    id_site bigint NOT NULL,
    localisation character varying(255),
    name_site character varying(255),
    officiel boolean,
    url_img character varying(255),
    id_user bigint
);
    DROP TABLE public.site;
       public         heap    postgres    false            �            1259    90667    site_id_site_seq    SEQUENCE     y   CREATE SEQUENCE public.site_id_site_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.site_id_site_seq;
       public          postgres    false    210            S           0    0    site_id_site_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.site_id_site_seq OWNED BY public.site.id_site;
          public          postgres    false    209            �            1259    90678    topo    TABLE     �   CREATE TABLE public.topo (
    id_topo bigint NOT NULL,
    date timestamp without time zone,
    description character varying(255),
    disponibilite boolean,
    nom character varying(255),
    id_site bigint,
    id_user bigint
);
    DROP TABLE public.topo;
       public         heap    postgres    false            �            1259    90686    users    TABLE     �  CREATE TABLE public.users (
    id_user bigint NOT NULL,
    account_non_expired boolean,
    account_non_locked boolean,
    credentials_non_expired boolean,
    email character varying(255) NOT NULL,
    enabled boolean,
    firstname character varying(255) NOT NULL,
    lastname character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    id_reservation bigint
);
    DROP TABLE public.users;
       public         heap    postgres    false            �            1259    90696    voie    TABLE     �   CREATE TABLE public.voie (
    id_voie bigint NOT NULL,
    hauteur_voie double precision,
    nom_voie character varying(255),
    secteur character varying(255),
    id_cote bigint,
    id_site bigint
);
    DROP TABLE public.voie;
       public         heap    postgres    false            �            1259    90694    voie_id_voie_seq    SEQUENCE     y   CREATE SEQUENCE public.voie_id_voie_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.voie_id_voie_seq;
       public          postgres    false    214            T           0    0    voie_id_voie_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.voie_id_voie_seq OWNED BY public.voie.id_voie;
          public          postgres    false    213            �
           2604    90656    longueur id_longueur    DEFAULT     |   ALTER TABLE ONLY public.longueur ALTER COLUMN id_longueur SET DEFAULT nextval('public.longueur_id_longueur_seq'::regclass);
 C   ALTER TABLE public.longueur ALTER COLUMN id_longueur DROP DEFAULT;
       public          postgres    false    206    205    206            �
           2604    90672    site id_site    DEFAULT     l   ALTER TABLE ONLY public.site ALTER COLUMN id_site SET DEFAULT nextval('public.site_id_site_seq'::regclass);
 ;   ALTER TABLE public.site ALTER COLUMN id_site DROP DEFAULT;
       public          postgres    false    210    209    210            �
           2604    90699    voie id_voie    DEFAULT     l   ALTER TABLE ONLY public.voie ALTER COLUMN id_voie SET DEFAULT nextval('public.voie_id_voie_seq'::regclass);
 ;   ALTER TABLE public.voie ALTER COLUMN id_voie DROP DEFAULT;
       public          postgres    false    214    213    214            �
           2606    90645    commentaire commentaire_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.commentaire
    ADD CONSTRAINT commentaire_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.commentaire DROP CONSTRAINT commentaire_pkey;
       public            postgres    false    203            �
           2606    90650    cotation cotation_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY public.cotation
    ADD CONSTRAINT cotation_pkey PRIMARY KEY (id_cote);
 @   ALTER TABLE ONLY public.cotation DROP CONSTRAINT cotation_pkey;
       public            postgres    false    204            �
           2606    90658    longueur longueur_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.longueur
    ADD CONSTRAINT longueur_pkey PRIMARY KEY (id_longueur);
 @   ALTER TABLE ONLY public.longueur DROP CONSTRAINT longueur_pkey;
       public            postgres    false    206            �
           2606    90663    reservation reservation_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.reservation DROP CONSTRAINT reservation_pkey;
       public            postgres    false    207            �
           2606    90677    site site_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY public.site
    ADD CONSTRAINT site_pkey PRIMARY KEY (id_site);
 8   ALTER TABLE ONLY public.site DROP CONSTRAINT site_pkey;
       public            postgres    false    210            �
           2606    90685    topo topo_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY public.topo
    ADD CONSTRAINT topo_pkey PRIMARY KEY (id_topo);
 8   ALTER TABLE ONLY public.topo DROP CONSTRAINT topo_pkey;
       public            postgres    false    211            �
           2606    90707 "   users uk_6dotkott2kjsp8vw4d0m25fb7 
   CONSTRAINT     ^   ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);
 L   ALTER TABLE ONLY public.users DROP CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7;
       public            postgres    false    212            �
           2606    90709 "   users uk_r43af9ap4edm43mmtq01oddj6 
   CONSTRAINT     a   ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);
 L   ALTER TABLE ONLY public.users DROP CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6;
       public            postgres    false    212            �
           2606    90693    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id_user);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    212            �
           2606    90704    voie voie_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY public.voie
    ADD CONSTRAINT voie_pkey PRIMARY KEY (id_voie);
 8   ALTER TABLE ONLY public.voie DROP CONSTRAINT voie_pkey;
       public            postgres    false    214            �
           1259    90705    index_user_role    INDEX     D   CREATE INDEX index_user_role ON public.roles USING btree (id_user);
 #   DROP INDEX public.index_user_role;
       public            postgres    false    208            �
           2606    90735 !   roles fk40d4m5dluy4a79sk18r064avh    FK CONSTRAINT     �   ALTER TABLE ONLY public.roles
    ADD CONSTRAINT fk40d4m5dluy4a79sk18r064avh FOREIGN KEY (id_user) REFERENCES public.users(id_user);
 K   ALTER TABLE ONLY public.roles DROP CONSTRAINT fk40d4m5dluy4a79sk18r064avh;
       public          postgres    false    2751    212    208            �
           2606    90765     voie fk4c02h16bmjsl2d76bofqc238u    FK CONSTRAINT     �   ALTER TABLE ONLY public.voie
    ADD CONSTRAINT fk4c02h16bmjsl2d76bofqc238u FOREIGN KEY (id_site) REFERENCES public.site(id_site);
 J   ALTER TABLE ONLY public.voie DROP CONSTRAINT fk4c02h16bmjsl2d76bofqc238u;
       public          postgres    false    210    2743    214            �
           2606    90740     site fk66lp6w0ubm1gfc47d099cjy8h    FK CONSTRAINT     �   ALTER TABLE ONLY public.site
    ADD CONSTRAINT fk66lp6w0ubm1gfc47d099cjy8h FOREIGN KEY (id_user) REFERENCES public.users(id_user);
 J   ALTER TABLE ONLY public.site DROP CONSTRAINT fk66lp6w0ubm1gfc47d099cjy8h;
       public          postgres    false    2751    212    210            �
           2606    90730 '   reservation fk6u59qafuvateecqro99vxji4a    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fk6u59qafuvateecqro99vxji4a FOREIGN KEY (id_user) REFERENCES public.users(id_user);
 Q   ALTER TABLE ONLY public.reservation DROP CONSTRAINT fk6u59qafuvateecqro99vxji4a;
       public          postgres    false    212    207    2751            �
           2606    90760     voie fkaea9uogl910h30lw7csj228f2    FK CONSTRAINT     �   ALTER TABLE ONLY public.voie
    ADD CONSTRAINT fkaea9uogl910h30lw7csj228f2 FOREIGN KEY (id_cote) REFERENCES public.cotation(id_cote);
 J   ALTER TABLE ONLY public.voie DROP CONSTRAINT fkaea9uogl910h30lw7csj228f2;
       public          postgres    false    204    2736    214            �
           2606    90755 !   users fkj9v5if6e7oj2xif15optacapj    FK CONSTRAINT     �   ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkj9v5if6e7oj2xif15optacapj FOREIGN KEY (id_reservation) REFERENCES public.reservation(id);
 K   ALTER TABLE ONLY public.users DROP CONSTRAINT fkj9v5if6e7oj2xif15optacapj;
       public          postgres    false    212    2740    207            �
           2606    90710 '   commentaire fkkgndecm5i0gjb9q0vr7c7qpir    FK CONSTRAINT     �   ALTER TABLE ONLY public.commentaire
    ADD CONSTRAINT fkkgndecm5i0gjb9q0vr7c7qpir FOREIGN KEY (id_site) REFERENCES public.site(id_site);
 Q   ALTER TABLE ONLY public.commentaire DROP CONSTRAINT fkkgndecm5i0gjb9q0vr7c7qpir;
       public          postgres    false    2743    210    203            �
           2606    90715 '   commentaire fkn6yfpv7fxw4jgjp0yl1g9eaw6    FK CONSTRAINT     �   ALTER TABLE ONLY public.commentaire
    ADD CONSTRAINT fkn6yfpv7fxw4jgjp0yl1g9eaw6 FOREIGN KEY (id_user) REFERENCES public.users(id_user);
 Q   ALTER TABLE ONLY public.commentaire DROP CONSTRAINT fkn6yfpv7fxw4jgjp0yl1g9eaw6;
       public          postgres    false    212    2751    203            �
           2606    90750     topo fkncul65ma0789762t4iukm4yt6    FK CONSTRAINT     �   ALTER TABLE ONLY public.topo
    ADD CONSTRAINT fkncul65ma0789762t4iukm4yt6 FOREIGN KEY (id_user) REFERENCES public.users(id_user);
 J   ALTER TABLE ONLY public.topo DROP CONSTRAINT fkncul65ma0789762t4iukm4yt6;
       public          postgres    false    212    211    2751            �
           2606    90745     topo fkq0u3kvcohifg7l9xe37aivo4i    FK CONSTRAINT     �   ALTER TABLE ONLY public.topo
    ADD CONSTRAINT fkq0u3kvcohifg7l9xe37aivo4i FOREIGN KEY (id_site) REFERENCES public.site(id_site);
 J   ALTER TABLE ONLY public.topo DROP CONSTRAINT fkq0u3kvcohifg7l9xe37aivo4i;
       public          postgres    false    2743    210    211            �
           2606    90725 '   reservation fkrxx2d19vx5ndessah3r5wit0c    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fkrxx2d19vx5ndessah3r5wit0c FOREIGN KEY (id_topo) REFERENCES public.topo(id_topo);
 Q   ALTER TABLE ONLY public.reservation DROP CONSTRAINT fkrxx2d19vx5ndessah3r5wit0c;
       public          postgres    false    211    207    2745            �
           2606    90720 $   longueur fksntp125c2624k8psbmjo0daru    FK CONSTRAINT     �   ALTER TABLE ONLY public.longueur
    ADD CONSTRAINT fksntp125c2624k8psbmjo0daru FOREIGN KEY (id_voie) REFERENCES public.voie(id_voie);
 N   ALTER TABLE ONLY public.longueur DROP CONSTRAINT fksntp125c2624k8psbmjo0daru;
       public          postgres    false    214    206    2753           