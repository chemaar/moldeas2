program even20
  integer :: a
  do a = 1, 20
    if ( mod(a,2) == 0.0 ) then 
    	print *, a
    end if	
  end do
end
