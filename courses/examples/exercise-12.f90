program mypow
  integer :: base, exponent
  real :: value
  integer :: i
  
  print*,"Intro the base..."
  read *, base

  print*,"Intro the exponent..."
  read *, exponent
 
  i = 0
  value = 1
  do while (i < exponent) 
    value = value * base
    i = i+1
  end do 

  print*,base,"to the power of ", exponent," = ",value 	
 
end program mypow


