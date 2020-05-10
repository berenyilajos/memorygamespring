package hu.fourdsoft.memorygame.service;

import hu.fourdsoft.memorygame.common.data.model.ResultData;
import hu.fourdsoft.memorygame.common.dto.ResultDTO;
import hu.fourdsoft.memorygame.common.dto.UserDTO;
import hu.fourdsoft.memorygame.common.dto.helper.DtoHelper;
import hu.fourdsoft.memorygame.common.model.Result;
import hu.fourdsoft.memorygame.common.model.User;
import hu.fourdsoft.memorygame.dao.ResultRepository;
import hu.fourdsoft.memorygame.data.dao.ResultDataRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResultServiceTest {

    @InjectMocks
    private ResultService underTest;

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private ResultDataRepository resultDataRepository;

    private Result testResult;

    private User testUser;

    private UserDTO testUserDTO;

    private List<Result> testResultList = new ArrayList<Result>();
    private List<Result> testResultListByUser = new ArrayList<Result>();

    private final int seconds = 100;
    private final String username = "username";
    private final String email = "email";
    private final String password = "password";

    @Before
    public void setUp() {
        testUser = new User(10L, username, email, password);
        testUserDTO = DtoHelper.toDTO(testUser);
        testResult = new Result(2L, testUser, new Date(), seconds);
        testResultListByUser.add(testResult);
        for (int i = 0; i < 5; i++) {
            Result result = new Result();
            result.setId(1L);
            result.setId(50);
            result.setResultDate(new Date());
            User user = new User();
            user.setUsername(username);
            result.setUser(user);
            testResultList.add(result);
        }
        when(resultRepository.findByUserOrderBySecondsAsc(eq(testUser), any(Pageable.class))).thenReturn(testResultListByUser);
        when(resultRepository.findAllByOrderBySecondsAscResultDateDescUserNameAscJoinFetchUser(any(Pageable.class))).thenReturn(testResultList);
        when(resultRepository.findAll()).thenReturn(testResultList);
    }

    @Test
    public void findAll() {
        List<ResultDTO> list = underTest.findAll();
        verify(resultRepository, times(1)).findAll();
        assertArrayEquals(DtoHelper.resultsToDTO(testResultList).toArray(), list.toArray());
    }

    @Test
    public void getResultsByUser() {
        List<ResultDTO> list = underTest.getResultsByUser(testUserDTO);
        verify(resultRepository, times(1)).findByUserOrderBySecondsAsc(eq(testUser), any(Pageable.class));
        assertArrayEquals(new ResultDTO[]{DtoHelper.toDTO(testResult)}, list.toArray());
    }

    @Test
    public void getAllResults() {
        List<ResultDTO> list = underTest.getAllResults();
        verify(resultRepository, times(1)).findAllByOrderBySecondsAscResultDateDescUserNameAscJoinFetchUser(any(Pageable.class));
        assertArrayEquals(DtoHelper.resultsToDTO(testResultList).toArray(), list.toArray());
    }

    @Test
    public void saveResult() {
        underTest.saveResult(seconds, testUserDTO);
        verify(resultRepository, times(1)).save(any(Result.class));
        verify(resultDataRepository, times(1)).save(any(ResultData.class));
    }

    @Test(expected = Exception.class)
    public void saveResultFailed() throws Exception {
        doThrow(Exception.class).when(resultRepository).save(any(Result.class));
        underTest.saveResult(seconds, testUserDTO);
        fail("Should not be called!");
    }

    @Test
    public void saveResultFailed2() throws Exception {
        String message = "Exception message";
        Exception ex = new RuntimeException(message);
        doThrow(ex).when(resultRepository).save(any(Result.class));
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() ->  underTest.saveResult(seconds, testUserDTO))
                .withMessage(message);
    }

    @Test
    public void saveResultFailed3() throws Exception {
        String message = "Exception message";
        Exception ex = new RuntimeException(message);
        doThrow(ex).when(resultRepository).save(any(Result.class));
        assertEquals(assertThrows(RuntimeException.class, () -> underTest.saveResult(seconds, testUserDTO)).getMessage(),
                message);
    }

}