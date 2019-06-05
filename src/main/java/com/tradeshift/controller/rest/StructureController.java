package com.tradeshift.controller.rest;

import com.tradeshift.exception.NodeMoveException;
import com.tradeshift.exception.NodeNotFoundException;
import com.tradeshift.model.Node;
import com.tradeshift.service.TreeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( value = "/tree")
@Api( value = "Amazing Company Structure", description = "Operations for company structure management")
public class StructureController {

    @Autowired
    private TreeService treeService;

    @ApiOperation(value = "Get first level children of a given node",
            notes = "if a given node is leaf of tree, list of its children is empty",
            response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully retrieved list of children"),
            @ApiResponse(code = 400, message = "node not found")
    })
    @GetMapping(value = "/{id}/children")
    public ResponseEntity<List> getChildren(@ApiParam(value = "Id of node stored in database table", required = true) @PathVariable Long id) {
        return ResponseEntity.accepted().body(treeService.getChildren(id));
    }

    @ApiOperation(value = "Change parent of a given node",
            notes = "By changing parent of a given node, heights of that node and all its children in all levels are updated.")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully change the parent of a given node"),
            @ApiResponse(code = 400, message = "node is not valid Or can't moved to it's descendant."),
            @ApiResponse(code = 500, message = "root node can not move")
    })
    @PatchMapping(value = "/{sourceId}/{destinationId}")
    public ResponseEntity changeParent(@ApiParam(value = "Change parent of node with id sourceId to the node with id destinationId", required = true) @PathVariable Long sourceId, @PathVariable Long destinationId) {
        treeService.changeParent(sourceId, destinationId);
        return ResponseEntity.accepted().build();
    }

    @ApiOperation(value = "Get root",
            response = Node.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully retrieved.")
    })
    @GetMapping(value = "/root")
    public ResponseEntity<Node> getRoot() {
        return ResponseEntity.accepted().body(treeService.getRoot());
    }

    @ApiOperation(value = "Replace older tree with given tree")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully replace older tree" ),
            @ApiResponse(code = 500, message = "Internal Server ERROR")
    })
    @PutMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity replaceNewTree(@ApiParam(value = "Node Json object to replace tree", required = true) @RequestBody Node rootNode) {
        treeService.insertNew(rootNode);
        return ResponseEntity.accepted().build();
    }
    @ExceptionHandler({ NodeNotFoundException.class , NodeMoveException.class})
    public ResponseEntity<String> handleBusinessException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}