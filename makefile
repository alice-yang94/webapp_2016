CONTROLLER_SRC  = main/Webgame/src/controller
MODEL_SRC       = main/Webgame/src/model
RM_SRC          = main/Webgame/src/rm
VIEW_SRC        = main/Webgame/src/View
WEBGAME_CLASSES = $(CONTROLLER_SRC)/*.class $(MODEL_SRC)/*.class $(RM_SRC)/*.class $(VIEW_SRC)/*.class

LOGIN_SRC       = login
MAIN_SRC        = main
SERVLET_CLASSES = $(LOGIN_SRC)/*.class $(MAIN_SRC)/*.class

RM_CP           = main/Webgame/src
SERVLET_CP		= WEB-INF/classes

all: $(WEBGAME_CLASSES) $(SERVLET_CLASSES) webgame.jar install

clean: 
			rm -rf $(WEBGAME_CLASSES) $(SERVLET_CLASSES) $(SERVLET_CP)/*.class rm/*.class main/webgame.jar

install: $(SERVLET_CLASSES)
			cp $(SERVLET_CLASSES) $(SERVLET_CP)

webgame.jar: $(WEBGAME_CLASSES)
			echo "Permissions: sandbox" >> manifest.txt
			jar cmf manifest.txt main/webgame.jar -C $(RM_CP) /controller/ -C $(RM_CP) /model/ -C $(RM_CP) /rm/ -C $(RM_CP) /View/
			rm -rf manifest.txt
			jarsigner -keystore keystore.jks main/webgame.jar hunted
			jarsigner -verify -verbose main/webgame.jar

$(CONTROLLER_SRC)/*.class: $(CONTROLLER_SRC)/*.java
			javac -cp $(RM_CP) $(CONTROLLER_SRC)/*.java

$(MODEL_SRC)/*.class: $(MODEL_SRC)/*.java
			javac -cp $(RM_CP) $(MODEL_SRC)/*.java

$(RM_SRC)/*.class: $(RM_SRC)/*.java
			javac -cp $(RM_CP) $(RM_SRC)/*.java

$(VIEW_SRC)/*.class: $(VIEW_SRC)/*.java
			javac -cp $(RM_CP) $(VIEW_SRC)/*.java

$(LOGIN_SRC)/*.class: $(LOGIN_SRC)/*.java
			javac $(LOGIN_SRC)/*.java

$(MAIN_SRC)/*.class: $(MAIN_SRC)/*.java
			javac $(MAIN_SRC)/*.java
