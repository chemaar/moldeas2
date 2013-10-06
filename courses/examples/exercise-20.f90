program addUp
 integer :: toRead = 0
 integer :: i = 0
 integer :: value, sum
 print *, "Enter the number of values to read..."
 read *, toRead 
 if (toRead > 0) then
   value = 0
   sum = 0
   i = 0
   do while (i<toRead)
	 print *, "Enter a value..."
	 read *, value 
 	 sum = sum + value
         i = i + 1
   end do
 end if
 print*, "The sum is ",sum
end program addUp

