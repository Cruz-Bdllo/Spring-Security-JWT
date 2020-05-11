# Spring Security usando JWT (JSON Web Token)
JWT es una forma mas para autenticarnos en un sitio web, a grandes rasgos su funcionamiento seria:
- Por primera vez el usuario ingresa sus datos en un formulario de registro teniendo por ejemplo los campos:

    - username.
    - email.
    - password.
    - roles.
Los valores ingresados se mandan al servidor para almacenarlos en alguna BD y a su vez regresando al usuario un mensaje de confirmación.
- El usuario se loguea mandando en un formulario de inicio de sesión **username** y **password** el servidor recibe estos valores y los valida con la BD para autenticarlos, si esta son correctos el servidor genera un **token** con ciertos valores (username, roles, etc) para identificar al usuario posteriormente y se lo regresa.
- Ahora cada vez que el usuario quiera acceder a ciertos recursos del sistema en su petición debera enviar junto a su cabecera (header) el token que anteriormente el servidor le envio. Para que este mismo pueda validar la autenticidad de la solicitud dejando acceder al recurso que solicito.
![diagrama][img-diagram]

## Resumen de JWT
Es un estandar abierto **(RFC 7519)** que define una forma compacta para transimir información de una manera segura entre las partes como un objeto **JSON** por lo cual esta información puede ser verificada y confiable ya que esta firmada digitalmente.

Los JWT pueden ser firmados mediante una palabra secreta usando el algoritmo **HMAC** o por medio de un par de llaves publica/privada usando **RSA** o **ECDSA**.

## Estructura de un JSON WEB TOKEN
En su forma compacta JSON Web Token consiste de tres partes separadas por un punto **(.)** los cuales son:

	- Header.
	- Payload.
	- Signatura.
Viendo se de esta forma [hhhh.pppp.ssss]

- Header: Normalmente consiste en dos partes: El tipo de token (el cual es JWT) y el algoritmo de firma que utiliza (**HMAC SHA256** o **RSA**).
- Payload: La segunda parte del token es el PAYLOAD el cual contiene los **claims** los cuales son declaraciones sobre una entidad (normalmente sobre el usuario) y algunos datos adicionales, hay 3 tipos de claims:

	- Claims registrados.
    - Claims públicos.
    - Claims privados.    
La estructura de un claims se veria de la siguiente forma:

    {
      "sub": "1234567890",
      "name": "John Doe",
      "admin": true
    }
	
    
- Signature: Para crear la firma se necesita tener el header y el payload codificados, una palabra secreta y el algoritmo que se usara para la firma (**HMAC SHA256**).

- Para profundizar mas sobre JWT, puedes consultar la documentación oficial: 
[ver documentación](https://jwt.io/introduction/)

## Práctica
Esta pequeña y sencilla practica se muestra el uso Spring Security usando JWT.
- Haciendo uso de **Spring initializr** [initializr](https://start.spring.io/) crear un proyecto con las siguientes dependencias:

    - Spring web.
    - Spring Security.
    - Spring Data JPA.
    - My Sql Driver.
Del repositorio central de **MAVEN** debemos agregar las dependencias de **JSON Web Token** el POM tendra la siguiente estructura:

~~~
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.9.1</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
~~~
- En el archivo de propiedades (**application.properties**) definimos las configuraciones para la conexión a la BD.
~~~
# Propiedades para la conexion a la BD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/[nombre_de_la_BD]
spring.datasource.username=root
spring.datasource.password=[tu_contraseña]
~~~
- Preparación de la estructura de paquetes (cabe mencionar que esta forma es la que a mi se me hace mas comoda, puedes optar por ordenarla como desees):
	- **controller**: Tendremos nuestro controlador principal para gestionar las rutas.
    - **entity**: Define las entidades que seran mapeadas a la BD.
    - **repository**: Interfaces para cada interfaz que extienden de **JpaRepository**.
    - **service**: Define la implementación de las interfaces.
    - **filter**: Clase que interceptara las peticiones del usuario para validar los token.
    - **payload**: Clases POJO's que usaremos para mapear los campos del Login y de Registro en nuestros formularios.
    - **security**: Clase de configuración de seguridad donde definimos las rutas protegidas.
    	- **service**: Contendra las clases que implementan las interfaces **UserDetails** y **UserDetailsService** para obtener y establecer los roles y la validación del formulario logion.
    	- **util**: Clase que crea, valida y desempaqueta los token que se validaran.
- Creamos las entidades Usuario y Roles, la primera define la tabla llamada **usuarios** y la segunda la tabla **roles**.
![er][img-mer]
- En el paquete de **repository** crearemos dos interfaces (corresponde a cada entidad creada) estas interfaces extienden de **JpaRepository**, para el caso de **UsuarioRepository** definiremos una consulta personalizada que permita buscar a un usuario por medio de su username `@Query("SELECT u FROM Usuario u WHERE u.username = ?1")`
- En el paquete **service** crearemos otras dos interfaces para cada entidad (IClaseService) en donde envolveremos los nombres que tiene JpaRepository para mayor comodidad.
    - Tambien crearemos dos clases que implementen cada interfaz con el fin de definir los métodos de **UsuarioRepository** y **RolesRepository**
    
- En el paquete **controller** creamos la clase **HomeController** (o como quieran nombrarla) y definimos las siguientes rutas:	
	
    - **/public**: de tipo GET en donde todos podran accesar a ella sin necesidad de loguearse.
    - **/registrarse**: de tipo POST donde se llenaran los campos username y password, esta tambien podrán acceder todos.
    - **/iniciar**: de tipo POST donde llanaran los campos username y password, esta tambien podrán acceder todos y es quien **crea** el token para el usuario.
    - **/home**: de tipo GET donde solo podrán acceder aquellos que estan autenticados.
    
- En el paquete security creamos la clase de configuración **SecurityConfigurer**, a esta clase le extenderemos la clase **WebSecurityConfigurerAdapter** y sobre escribiremos los métodos:
	- **config(AuthenticationManagerBuilder auth)**: En este método definiremos el tipo de autenticado (que sera atraves de la BD).
    - **config(HttpSecurity http)**: Aqui estableceremos la reestricción de las rutas (publicas o privadas), ademas de definir un filtro personalizado de peticiones que nos ayude a analizar los token que manden los usuario en sus peticiones.

 Tambien aqui crearemos nuestros **Beans**
- En el paquete **service** que esta dentro del paquete **security** Crearemos las siguientes clases:
	
    - **MiUserDetails**: Esta clase implementa la interfaz *UserDetails* e implementa sus métodos para asignar username, password, authorities, active, etc.
    - **MiUserDetailsService**: Implmenta la interfaz *UserDetailsService* que implementa el método **loadUserByUsername** en el cual busca el registro en la base de datos y devuelve un objeto UserDetails que asigna todas las propiedades que se encontrarón en la BD.
- En el paquete **utils** que esta dentro del paquete **security** Crearemos la siguiente clase:
	- JwtUtil: Esta clase __@Component__ desempaqueta el token enviado por el usuario extrayendo los CLAIMS del token que contienen los datos de autenticación (username y/o roles), tambien valida si el tiempo de vida del token aun es valido y por ultimo permite crear un nuevo token.
- En el paquete **payload** se crean las clases:

    - **AutenticacionLogin**: Que solamente mapea los campos del formulario para el inicio de sesión (username, password).
    - **AutenticacionResponse**: Asigna el token creado a su propiedad ya que el filtro requiere que respuesta es la que dara al usuario.

- Por ultimo en el paquete **filter** contiene la clase __AuthFiltroToken__ el cual intercepta las peticiones que manda el usuario antes de llegar al controlador con la finalidad de analizar el token que se envio y validar su autenticidad creando un contexto de seguridad.

## Imagenes de resultado
### Página pública
![public][img-public]

### Registro
![registro][img-registro]

### Inicio de sesión
![inicio][img-login]

### Página privada
![privada][img-path-sec]


[img-diagram]:https://grokonez.com/wp-content/uploads/2018/12/angular-nodejs-jwt-authentication-example-work-process-diagram.png "diagrama de proceso JWT"
[img-mer]: http://www.goog.com "diagrama de Entidad Relación"
[img-public]: http://www.goog.com "diagrama de Entidad Relación"
[img-registro]: http://www.goog.com  "Registro"
[img-login]: https://drive.google.com/open?id=12QNI4XOqnsv8Fv1AL39sPtkfjobA2BuF  "Iniciar sesión"
[img-path-sec]: http://www.goog.com "Ruta protegida"