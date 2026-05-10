package com.cheonan.matzip.service;

import com.cheonan.matzip.dao.MemberDao;
import com.cheonan.matzip.dto.MemberJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;

    public void join(MemberJoinRequest request) {

        memberDao.join(request);
    }
}