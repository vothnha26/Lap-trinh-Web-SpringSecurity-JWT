$(document).ready(function () {
    // Check if user is already logged in and redirect to profile
    if (localStorage.token && window.location.pathname === '/login') {
        window.location.href = "/user/profile";
    }

    // Show loading spinner on profile page
    if (window.location.pathname === '/user/profile') {
        // Hiển thị thông tin ngay lập tức
        showProfileContent();
        
        // Sau đó mới gọi API để cập nhật thông tin
        setTimeout(() => {
            loadUserProfile();
        }, 100);
    }

    // Load user profile data from database
    function loadUserProfile() {
        console.log('Loading user profile from database...');
        console.log('Token:', localStorage.token ? 'Present' : 'Missing');
        
        // Nếu không có token, hiển thị thông tin mặc định
        if (!localStorage.token) {
            console.log('No token found, showing default profile');
            showDefaultProfile();
            return;
        }
        
        $.ajax({
            type: 'GET',
            url: '/users/me',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.token);
                console.log('Authorization header set');
            },
            success: function (data) {
                console.log('Profile data received from database:', data);
                
                // Hiển thị thông tin từ database
                $('#profileName').text(data.fullName || 'N/A');
                $('#profileEmail').text(data.email || 'N/A');
                $('#profileImage').attr("src", data.images || 'https://via.placeholder.com/150/667eea/ffffff?text=User');
                
                if (data.createdAt) {
                    const createdDate = new Date(data.createdAt);
                    $('#profileCreatedAt').text(createdDate.toLocaleDateString());
                }
                
                // Lưu thông tin vào localStorage để dùng sau
                localStorage.setItem('userInfo', JSON.stringify({
                    fullName: data.fullName,
                    email: data.email,
                    images: data.images,
                    createdAt: data.createdAt
                }));
                
                showProfileContent();
                console.log('Profile displayed successfully from database');
            },
            error: function (xhr) {
                console.log('Error loading profile from database:', xhr.status, xhr.responseText);
                
                let errorMessage = 'Sorry, you are not logged in! Please login again.';
                if (xhr.status === 401) {
                    errorMessage = 'Session expired. Please login again.';
                } else if (xhr.status === 403) {
                    errorMessage = 'Access denied. Please login again.';
                }
                
                showAlert('error', errorMessage);
                setTimeout(() => {
                    window.location.href = "/login";
                }, 2000);
            }
        });
    }

    // Hiển thị thông tin profile mặc định
    function showDefaultProfile() {
        console.log('Showing default profile');
        
        // Lấy thông tin từ localStorage nếu có
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
        
        $('#profileName').text(userInfo.fullName || 'Demo User');
        $('#profileEmail').text(userInfo.email || 'demo@example.com');
        $('#profileImage').attr("src", userInfo.images || 'https://via.placeholder.com/150/667eea/ffffff?text=Demo');
        $('#profileCreatedAt').text(userInfo.createdAt ? new Date(userInfo.createdAt).toLocaleDateString() : new Date().toLocaleDateString());
        
        showProfileContent();
        console.log('Default profile displayed');
    }

    // Hiển thị nội dung profile
    function showProfileContent() {
        console.log('Showing profile content');
        $('#loadingSpinner').hide();
        $('#profileCard').show();
        $('#statsCards').show();
        $('#featureCards').show();
        console.log('Profile content displayed');
    }

    // Show alert function
    function showAlert(type, message) {
        const alertClass = type === 'error' ? 'alert-danger' : 'alert-success';
        const icon = type === 'error' ? 'fas fa-exclamation-triangle' : 'fas fa-check-circle';
        
        const alertHtml = `
            <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
                <i class="${icon} me-2"></i>${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
        
        $('#alertContainer').html(alertHtml);
        
        // Auto-dismiss after 5 seconds
        setTimeout(() => {
            $('.alert').alert('close');
        }, 5000);
    }

    // Set button loading state
    function setButtonLoading(buttonId, isLoading) {
        const $button = $(`#${buttonId}`);
        const $loading = $button.find('.loading');
        const $normal = $button.find('.normal');
        
        if (isLoading) {
            $loading.show();
            $normal.hide();
            $button.prop('disabled', true);
        } else {
            $loading.hide();
            $normal.show();
            $button.prop('disabled', false);
        }
    }

    // Login function
    $('#Login').click(function () {
        const email = $('#email').val().trim();
        const password = $('#password').val().trim();

        if (!email || !password) {
            showAlert('error', 'Please fill in all fields.');
            return;
        }

        setButtonLoading('Login', true);

        $.ajax({
            type: 'POST',
            url: '/auth/login',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({ 
                email: email, 
                password: password 
            }),
            success: function (data) {
                localStorage.setItem('token', data.token);
                
                // Lưu thông tin user vào localStorage
                const userInfo = {
                    fullName: data.user?.fullName || 'Nguyễn Văn A',
                    email: data.user?.email || email,
                    images: data.user?.images || 'https://via.placeholder.com/150/667eea/ffffff?text=User',
                    createdAt: data.user?.createdAt || new Date().toISOString()
                };
                localStorage.setItem('userInfo', JSON.stringify(userInfo));
                
                showAlert('success', 'Login successful! Redirecting...');
                setTimeout(() => {
                    window.location.href = "/user/profile";
                }, 1500);
            },
            error: function (xhr) {
                setButtonLoading('Login', false);
                let errorMessage = 'Login failed. Please check your credentials.';
                
                if (xhr.responseJSON && xhr.responseJSON.detail) {
                    errorMessage = xhr.responseJSON.detail;
                }
                
                showAlert('error', errorMessage);
            }
        });
    });

    // Register function
    $('#Register').click(function () {
        console.log('Register button clicked');
        const fullName = $('#regFullName').val().trim();
        const email = $('#regEmail').val().trim();
        const password = $('#regPassword').val().trim();

        console.log('Form data:', { fullName, email, password: '***' });

        if (!fullName || !email || !password) {
            showAlert('error', 'Please fill in all fields.');
            return;
        }

        if (password.length < 6) {
            showAlert('error', 'Password must be at least 6 characters long.');
            return;
        }

        console.log('Starting registration...');
        setButtonLoading('Register', true);

        $.ajax({
            type: 'POST',
            url: '/auth/signup',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({ 
                fullName: fullName,
                email: email, 
                password: password 
            }),
            success: function (data) {
                console.log('Registration successful:', data);
                setButtonLoading('Register', false);
                
                // Lưu thông tin user vào localStorage
                const userInfo = {
                    fullName: data.fullName || fullName,
                    email: data.email || email,
                    images: data.images || 'https://via.placeholder.com/150/667eea/ffffff?text=User',
                    createdAt: data.createdAt || new Date().toISOString()
                };
                localStorage.setItem('userInfo', JSON.stringify(userInfo));
                
                showAlert('success', 'Account created successfully! You can now login.');
                $('#registerModal').modal('hide');
                $('#registerForm')[0].reset();
            },
            error: function (xhr) {
                console.log('Registration error:', xhr.status, xhr.responseText);
                setButtonLoading('Register', false);
                let errorMessage = 'Registration failed. Please try again.';
                
                if (xhr.responseJSON && xhr.responseJSON.detail) {
                    errorMessage = xhr.responseJSON.detail;
                } else if (xhr.status === 400) {
                    errorMessage = 'Email already exists or invalid data.';
                }
                
                showAlert('error', errorMessage);
            }
        });
    });

    // Show register modal
    $('#showRegister').click(function (e) {
        e.preventDefault();
        console.log('Register button clicked');
        console.log('Modal element:', $('#registerModal').length);
        $('#registerModal').modal('show');
        console.log('Modal show called');
    });

    // Refresh profile
    $('#refreshProfile').click(function (e) {
        e.preventDefault();
        $('#loadingSpinner').show();
        $('#profileCard').hide();
        loadUserProfile();
    });

    // Logout function
    $('#Logout').click(function (e) {
        e.preventDefault();
        
        // Show confirmation
        if (confirm('Are you sure you want to logout?')) {
            localStorage.clear();
            showAlert('success', 'Logged out successfully! Redirecting...');
            setTimeout(() => {
                window.location.href = "/login";
            }, 1500);
        }
    });

    // Load profile data when on profile page
    if (window.location.pathname === '/user/profile') {
        loadUserProfile();
    }

    // Handle form submissions
    $('#loginForm').on('submit', function(e) {
        e.preventDefault();
        $('#Login').click();
    });

    $('#registerForm').on('submit', function(e) {
        e.preventDefault();
        $('#Register').click();
    });

    // Auto-focus on first input
    if (window.location.pathname === '/login') {
        $('#email').focus();
    }

    // Handle Enter key in forms
    $('input').on('keypress', function(e) {
        if (e.which === 13) {
            if (window.location.pathname === '/login') {
                $('#Login').click();
            } else if ($('#registerModal').hasClass('show')) {
                $('#Register').click();
            }
        }
    });
});
