JAVAC=javac
SRCDIR=src
SRCEXT=java
CLSEXT=class
sources = $(wildcard $(SRCDIR)/*.$(SRCEXT))
classes = $(sources:.$(SRCEXT)=.$(CLSEXT))
RM = rm
RMFLAGS = -f
RMTARGETS = *.$(CLSEXT)

all: $(classes)

%.$(CLSEXT): %.$(SRCEXT)
	$(JAVAC) $<

clean:
	$(RM) $(RMFLAGS) $(RMTARGETS)
