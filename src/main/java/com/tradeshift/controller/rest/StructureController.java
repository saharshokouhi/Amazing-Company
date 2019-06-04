package com.tradeshift.controller.rest;

import com.tradeshift.model.Node;
import com.tradeshift.service.GraphService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping( value = "/tree")
@Api( value = "Amazing company structure", description = "Operations for company structure management")
public class StructureController {

    @Autowired
    private GraphService graphService;


    @ApiOperation(value = "View first level children of a given node",
            notes = "if a given node is leaf of tree, list of its children is empty",
            response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully retrieved list of children"),
            @ApiResponse(code = 500, message = "node not found")
    })
    @GetMapping(value = "/{id}/children")
    public ResponseEntity<List> getChildren(@ApiParam(value = "Node object store in database table", required = true) @PathVariable Long id) {
        return ResponseEntity.accepted().body(graphService.getChildren(id));
    }

    @ApiOperation(value = "Change parent of a given node",
            notes = "By changing parent of a given node, heights of that node and all its children in all levels are updated.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully change the parent of a given node"),
            @ApiResponse(code = 500, message = "node is not valid")
    })
    @PatchMapping(value = "/{sourceId}/{destinationId}")
    public ResponseEntity changeParent(@ApiParam(value = "ChangeParent dao object contains source node id and destination source id", required = true) @PathVariable Long sourceId, @PathVariable Long destinationId) {
        graphService.changeParent(sourceId, destinationId);
        return ResponseEntity.accepted().build();
    }

    @ApiOperation(value = "Make new tree with given node")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully create new tree and insert to database"),
            @ApiResponse(code = 401, message = "root Object is not valid"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PutMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity replaceNewTree(@ApiParam(value = "Node object to insert new tree in database", required = true) @RequestBody Node rootNode) {
        graphService.insertNew(rootNode);
        return ResponseEntity.accepted().build();
    }

}