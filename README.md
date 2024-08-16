# Practicing to build Microservices with Spring-boot, Keycloak, and SSL integrated
This project was created to practice building Microservices with Spring-boot, Securing services with Keycloak and testing the performance of hibernate-orm and hibernate-reactive.
In this project, there is a sample of the Rest-API project that contains many samples of Endpoint classes and samples of hibernate queries, etc.
If you are interested, let's see a couple of documentation below.


## Introduction
This project is build by Spring-boot, you can see what is Spring-boot on their [official website](https://docs.spring.io/spring-boot/index.html).
Goals of this project is <strong>"to learn and find a better way how to build a Microservices System Architecture using Spring-boot"</strong>,
so the description on the ``README.md`` file will be changed periodically following updates of this project. <br/>
There are some important things you should know about this project
1. Root Folder description
    - [`ms-persistent-base`](/ms-persistent-base) contains Base classes to creating Entity, Payload, or Response
    - [`ms-security-base`](/ms-security-base) is a spring-boot project that contains the Security Configuration will be used in all services
    - [`spring-boot-ms-integration-test`](/spring-boot-ms-integration-test) is a spring-boot project that contains the Security Configuration will be used in all services
    - [`docker`](/docker) contains files that needed to build Container of Keycloak and PostgresQL
    - [`docs`](/docs) contains image assets and markdown (`.md`) files of project documentation
    - [`nginx`](/nginx) contains files that needed to run the Nginx
2. Incomplete parts
    1. Performance test with JMeter is totally not created yet.
       This part will test some API endpoint from Rest Sample and Rest-Sample Reactive services to compare their performance
    2. Documentation of the Architecture
3. This project is developed and tested on MacOS, it might not be working properly on Windows OS

<hr/>

## Prerequisites
To follow this guide, you need:
1. Understand Java
2. Understand Object Oriented Programming
3. Understand Docker
4. OpenJDK 21+ installed
5. Apache Maven 3.9.6 or newer
6. Docker

> :warning: It would be better you install ``sdkman`` on your computer, and then use ``sdkman`` to install OpenJDK
> <br/> [See this to install ``SDKMAN`` ](https://sdkman.io/install)

<hr/>

## How to setup ?
Please follow these steps to run this project correctly
### 1. Clone the project
There is no special way to clone this project

### 2. Localhost setup
We need to setup our localhost or our Local Machine to make a DNS running on our Local Machine

#### - Linux / MacOS
1. Open your terminal
2. Type ``sudo vi /etc/hosts`` and input your computer password
3. Press i on keyboard and type this on new line
   ```shell
   ...
   
   10.123.123.123 <domain name that you want>
   10.123.123.123 keycloak.<domain name that you want>
   ```
   it would be like
    ```shell
   ...
   
   10.123.123.123 example.com 
   10.123.123.123 keycloak.example.com 
    ```
   or
    ```shell
   ...
   
   10.123.123.123 practicing-spring-boot.com
   10.123.123.123 keycloak.practicing-spring-boot.com 
    ```
   or something else
4. Press ``Esc`` on keyboard, then press ``Shift`` + ``Z`` twice

#### - Windows
- Coming soon

### 3. Environment Variables Setup
1. Please copy ``docker_env.env.example`` to ``docker_env.env``
2. Fill in these variable first
    - ``SERVER_HOST`` is the domain name that you have added before to ``/etc/hosts``
    - ``KEYCLOAK_HOST`` is your domain name with prefix ``keycoak.``
    - All environment variables with prefix ``DATABASE_``
    - ``KEYCLOAK_EXTERNAL_PORT`` is accessible port of keycloak container
    - ``KEYCLOAK_ADMIN`` and ``KEYCLOAK_ADMIN_PASSWORD`` is a user credential will be used to sign in to keycloak admin console
    - ``KEYCLOAK_KEYSTORE_PASSWORD`` keystore password to secure the keycloak

### 4. Create local DNS
This step is to make our domain works on our local
1. Open your terminal
2. Navigate to this project root folder
3. Type ``./create-local-dns.sh`` and enter
4. The result would be like this <br/>
   ![image](/docs/img/create-local-dns.png)
5. To make sure your domain works, type this on terminal,
   ```shell
   ping 10.123.123.123
   ```
   and
   ```shell
   ping <your domain>
   ```
   if it works, the result would be like this
   ```text
   PING q-learning.com (10.123.123.123): 56 data bytes
   64 bytes from 10.123.123.123: icmp_seq=0 ttl=64 time=0.118 ms
   64 bytes from 10.123.123.123: icmp_seq=1 ttl=64 time=0.299 ms
   64 bytes from 10.123.123.123: icmp_seq=2 ttl=64 time=0.296 ms
   64 bytes from 10.123.123.123: icmp_seq=3 ttl=64 time=0.220 ms
   ```

### 5. Create certificate for keycloak and SSL
Certificate is needed to secure our keycloak
1. Run ``./create-certificate.sh`` on your terminal
2. After that check on folder ``./nginx/certs``, ``self-signed.crt`` and ``self-signed.key`` files would be there <br/>
   ![image](/docs/img/nginx-certs.png)
3. And then check on folder ``./docker/keycloak/``, ``server.keystore`` would be there
   ![image](/docs/img/server-keystore.png) <br/>
   (don't worry about Dockerfile, it would be generated letter)

### 6. Run Keycloak and PostgreSQL
1. On your terminal, please run this ``./compose-up-keycloak-posgress.sh``
2. Please wait until finish, the result would be like this
   ![image](/docs/img/compose-up-keycloak-postgress.png)

### [7. Setup keycloak](/docs/keycloak-setup.md)
Click [the title](/docs/keycloak-setup.md) to see the complete instruction
