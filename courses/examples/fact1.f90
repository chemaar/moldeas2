program fact1
	REAL, DIMENSION(20) :: fact
	INTEGER :: n, nmax = 20
	fact(1) = 1.0
	do n = 2, nmax
		fact(n) = fact(n-1) * n
	end do
	print*,fact
end
