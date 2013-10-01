program cube
	IMPLICIT NONE
	integer :: a, b
	do a = 1, 8
		b = a*a*a
		print*,a,"to the power of three = ",b
	end do
end
