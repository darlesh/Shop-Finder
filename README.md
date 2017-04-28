# Shop-Finder

The Shop Finder is spring boot RESTFful application which consist different services to perstist the shop data and getting nearest shop from using longitude and latitude to persist data this application use h2(In memory database.

Getting Started

To run this spring boot gradle application on local machine

Prerequisites :

java jdk 1.6 or above
IDE (eclispe or intellij idea)

Built With:

Gradle - Dependency Management

Note:
if you are using eclispe then you need install gradle for eclispe.

Installing

please clone the git using following command

   git clone https://github.com/darlesh/Shop-Finder.git
   
Once git cloning is finish refresh build.gradle file this after all dependencies get downloaded.

Running

Right click run as spring boot application.

To add an shop you can use below api :

1.  http://localhost:8080/ShopInsert?shopName=Dominos&address=Delhi

If want search near by shops then use below api : this will return nearest shops :

2.  http://localhost:8080/ShopNear?longitude=18.5204303&latitude=73.8567437

where, lat = latitude lng = longitude
