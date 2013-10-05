program asc20
  integer :: a
  do a = 1, 20
   print*,a
  end do

 a = 1 	
 while (a <= 20) do
   print*,a
  end do

 a = 1 
 do while (a <= 20) 
   print*,a
 end do

 a = 1 
 do
   print*,a
   a = a+1 
 until (a > 20)

end
