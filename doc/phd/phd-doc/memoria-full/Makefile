.PHONY: clean

all:	
	rubber -q memoria
	makeglossaries memoria
	rubber -q --pdf memoria	

clean:  
	rm -f *.pdf
	rm -f *.gl*
	rm -f *.idx
	rm -f *.ist
	rm -f *.out
	rubber --clean memoria

