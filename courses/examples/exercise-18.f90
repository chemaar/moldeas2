program isPrimeNumber
  integer :: number
  integer :: i
  logical :: isPrime = .TRUE.
  
  print*,"Intro a number..."
  read *, number
  
  if (number .NE. 1) then 
	  i = 2
	 
	  do while ( (isPrime) .AND. (i .NE. number)) 
		if (mod(number,i) == 0.0) then
			isPrime = .FALSE.
		endif
	    i = i+1
	  end do 
  endif
  print*,"The number ", number," is prime ",isPrime
 
end program isPrimeNumber

