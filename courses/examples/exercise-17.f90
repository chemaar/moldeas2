program holidayTree
 integer :: baseStars = 15
 integer :: halfBlankSpaces = 0
 integer :: i, j, k

 i = 1
 do while (i<baseStars)
        halfBlankSpaces = (baseStars-i)/2
	j = 0  
	do while (j<halfBlankSpaces)
	  write(*,"(A)",advance="no") " "
	  j = j + 1
	end do

	k = 0  
	do while (k<i)
	  write(*,"(A)",advance="no") "*"
	  k = k + 1
	end do

	j = 0  
	do while (j<halfBlankSpaces)
	  write(*,"(A)",advance="no") " "
	  j = j + 1
	end do
   print*,""
   i = i + 2
 end do
 
 halfBlankSpaces = (baseStars/2)-1;

 i = 0
 do while (i<3)
	j = 0  
	do while (j<halfBlankSpaces)
	  write(*,"(A)",advance="no") " "
	  j = j + 1
	end do

	k = 0  
	do while (k<3)
	  write(*,"(A)",advance="no") "*"
	  k = k + 1
	end do

	j = 0  
	do while (j<halfBlankSpaces)
	  write(*,"(A)",advance="no") " "
	  j = j + 1
	end do
   print*,""
   i = i + 1
 end do

end program holidayTree




