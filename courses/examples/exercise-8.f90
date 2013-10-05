program sum20
  integer :: sum,a
  sum = 0
  do a = 1, 20
   sum = sum + a    
  end do
  print*,sum

 a = 1 
 sum = 0
 do while (a <= 20) 
   sum = sum + a    
   a = a+1
 end do

 print*,sum

end program sum20
