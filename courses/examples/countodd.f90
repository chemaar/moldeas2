program countodd
  integer :: a, count
  count = 0
  do a = 1, 20
    if ( mod(a,2)  /= 0.0 ) then 
    	count = count + 1 
    end if	
  end do
 print *, count 
end
