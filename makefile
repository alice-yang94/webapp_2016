CONTROLLER_SRC  = Webgame/src/controller
MODEL_SRC       = Webgame/src/model
RM_SRC          = Webgame/src/rm
VIEW_SRC        = Webgame/src/View
WEBGAME_CLASSES = $(CONTROLLER_SRC)/*.class $(MODEL_SRC)/*.class $(RM_SRC)/*.class $(VIEW_SRC)/*.class

LOGIN_SRC       = login
MAIN_SRC        = main
SERVLET_CLASSES = $(LOGIN_SRC)/*.class $(MAIN_SRC)/*.class

RM_CP           = Webgame/src
SERVLET_CP			= WEB-INF/classes

all: $(WEBGAME_CLASSES) $(SERVLET_CLASSES) install

clean: 
			rm -rf $(WEBGAME_CLASSES) $(SERVLET_CLASSES) $(SERVLET_CP)/*.class rm/*.class

install: $(SERVLET_CLASSES)
			cp $(SERVLET_CLASSES) $(SERVLET_CP)

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