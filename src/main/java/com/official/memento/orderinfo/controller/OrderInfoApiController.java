package com.official.memento.orderinfo.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.orderinfo.controller.dto.ToDoUpdateOrderRequest;
import com.official.memento.orderinfo.service.command.ToDoPositionUpdateCommand;
import com.official.memento.orderinfo.service.usecase.OrderInfoUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order-info")
@RequiredArgsConstructor
public class OrderInfoApiController implements OrderInfoApiDocs {

    private final OrderInfoUpdateUseCase orderInfoUpdateUseCase;

    @Override
    @PatchMapping("todo/{toDoId}/order")
    public ResponseEntity<SuccessResponse<?>> updateToDoOrder(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long toDoId,
            @RequestBody final ToDoUpdateOrderRequest request
    ) {
        orderInfoUpdateUseCase.updatePosition(
                ToDoPositionUpdateCommand.of(
                        authorizationUser.memberId(),
                        toDoId,
                        request.date(),
                        request.previousToDoId(),
                        request.nextToDoId()
                )
        );

        return SuccessResponse.of(
                HttpStatus.OK,
                "투두 드래그앤드랍 성공"
        );
    }
}
