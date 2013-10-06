program squares
  integer :: a, b
  do a = 1, 20
    b = a**2
    print*,a,"to the power of two = ",b
  end do
  a = 1
  b = 0
  do while (a <= 20) 
   b = a**2
   print*,a,"to the power of two = ",b
   a = a+1
 end do

end program squares
