program Fibonacci
  IMPLICIT NONE
  integer:: F1 = 1, F2 = 0, i = 0, n = 0
  print *, "Enter a number..."
  read (*,*) n
  do 
      if (i >= n) exit
	F1 = F2 + F1
	print *, F1
	i = i + 1
      if (i >= n) exit
	F2 = F2 + F1
	print *, F2
	i = i + 1
    end do
end
