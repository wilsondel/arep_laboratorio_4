# Taller 3

Se construye un servidor web para soportar una funcionalidad similar a la de Spark. 
La aplicación permite el registro de servicios get y post usando funciones lambda. 
Permite configurar el directorio de los archivos estáticos, y 
cambiar el tipo de la respuesta a "application/json".

### Instalando
Descarga o clona el repositorio y ejecuta
```
mvn package
```

luego puedes ejecutar desde tu IDE o en la terminal

```
java -cp .\target\classes\ edu.eci.arsw.webapps.FirstApp
```
Posteriormente ingresando a localhost:35000 se puede ver:

![img_1.png](img/img_1.png)

Luego, puedes ingresar a http://localhost:35000/apps/hello donde podrás encontrar

![img.png](img/img_6.png)

Dando click sobre el botón se ve que sale una venta lo que junto con el color del background nos permite evidenciar el
correcto funcionamiento de los servicios para retornar HTML, CSS y JS del servidor.

![img_1.png](img/img_7.png)

Ahora, se valida el registro de servicios get y post usando funciones lambda.
Para validar los servicios get se ingresa a  http://localhost:35000/spark/spark:

![img.png](img/img_11.png)

O también a http://localhost:35000/spark/mytestla cual lee y retorna un archivo del servidor 

![img_1.png](img/img_10.png)

Para validar los servicios post se ingresa a  http://localhost:35000/spark/post:

![img_2.png](img/img_20.png)

Se envían los datos, y se evidencia una respuesta por part del servidor con los datos ingresados:

![img_3.png](img/img_30.png)

La configuración de archivos estáticos se realiza con 

```
staticFiles("/public");
```

en FirstApp, para probarlo se puede ingresar a las siguientes rutas que traeran archivos json,js,css,html respectivamente:

![img_4.png](img/img_40.png)

![img_5.png](img/img_50.png)

![img_6.png](img/img_60.png)

![img_7.png](img/img_70.png)

Para cambiar el tipo de respuesta por application/json se realiza por ejemplo:

```
get("/message", "application/json",(request,response) -> new JSONSparkTestService().getResponse(request, response) );
```

Y se evidencia accediendo a:

![img_8.png](img/img_80.png)


## Corriendo tests

Para correr las pruebas puedes des tu IDE o usando el comando

```
mvn test
```

![img_2.png](img/img_2.png)



Para las pruebas se hizo uso del nombramiento given_When_Then

```
givenAvalueWhenSaveInCacheThenReturnIt()
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Versioning

*  Se hizo uso de git para el manejo de versionamiento.

## Authors

* **Wilson Alirio Delgado Hernández** 

