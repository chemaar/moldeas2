.PHONY: clean

all:	rubber memoria
	makeglossaries memoria
	rubber --pdf memoria
	xpdf memoria.pdf

clean:  rm -f *.pdf
	rm -f *.gl*
	rm -f *.idx
	rm -f *.ist
	rm -f *.out
	rubber --clean memoria

