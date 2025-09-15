import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

const AuthRedirectHandler = () => {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    useEffect(() => {
        const accessToken = searchParams.get("accessToken");
        const refreshToken = searchParams.get("refreshToken");

        if (accessToken && refreshToken) {
            console.log("[debug] >>>> 리다이렉트 로그인");
            console.log("accessToken:", accessToken);
            console.log("refreshToken:", refreshToken);

            // ⭐ 토큰을 localStorage에 저장해요. ⭐
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);

            // ⭐ 이제 메인 페이지로 이동합니다! ⭐
            navigate("/main", { replace: true });
        } else {
            // 토큰이 없으면 로그인 페이지로 돌아가요.
            navigate("/login");
        }
    }, [navigate, searchParams]);

    return (
        <div>
            <p>로그인 처리 중입니다. 잠시만 기다려 주세요...</p>
        </div>
    );
};

export default AuthRedirectHandler;
