program showevennumbers
  integer :: a
  do a = 0, 20
    if ( mod(a,2)  == 0.0 ) then 
	 print *, a 
    end if	
  end do

 a = 0
 sum = 0
 do while (a <= 20) 
   if ( mod(a,2)  == 0.0 ) then 
     print *, a 
   end if	
   a = a+1
 end do
end program showevennumbers
