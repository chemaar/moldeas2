program cubes
  integer :: a, b
  integer :: exponent
  exponent = 3
  do a = 1, 20
    b = a**exponent
    print*,a,"to the power of two = ",b
  end do
  a = 1
  b = 0
  do while (a <= 20) 
   b = a**exponent
   print*,a,"to the power of ", exponent," = ",b
   a = a+1
 end do
end program cubes
